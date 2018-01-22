package org.summerframework.core.spi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.loader.PropertiesLoader;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.commons.util.ResourceUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class SPILoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(SPILoader.class);
	private static final String SPI_DIR = "META-INF/summer/spi";
	private static final Map<Class<?>, List<SPIMapper>> SPI_MAPPERS = Maps.newHashMap();
	private static final AtomicBoolean LOADED = new AtomicBoolean(false);
	private static final ReentrantLock LOCK = new ReentrantLock();
	private static final Set<JarFile> JAR_FILES = Sets.newHashSet();

	protected SPILoader() {

	}

	protected static void loading() {
		final ReentrantLock lock = LOCK;
		SPILoader loader = null;
		Map<String, List<InputStream>> streams = null;

		try {
			lock.lock();
			loader = new SPILoader();
			final Enumeration<URL> resources;
			try {
				resources = loader.getResources();
			} catch (final Throwable e) {
				throw new SPIException("加载资源异常: " + e.getMessage(), e);
			}

			final SPIResource spiResource;
			try {
				spiResource = loader.getSPIResource(resources);

			} catch (final Throwable e) {
				throw new SPIException("获取SPI资源异常:" + e.getMessage(), e);
			}

			final Map<Class<?>, List<SPIMapper>> spiMappers = Maps.newHashMap();

			loader.getSPIMapper(spiResource.getFiles(), spiMappers);

			streams = spiResource.getStreams();
			loader.getSPIMapperWithStream(streams, spiMappers);
			loader.sortSPIMapper(spiMappers);
			
			SPI_MAPPERS.clear();
			SPI_MAPPERS.putAll(spiMappers);
			LOADED.set(true);
		} finally {
			if (loader != null) {
				if (streams != null) {
					loader.closeStream(streams.values());
				}
				loader.closeJarFile();
			}
			lock.unlock();
		}
	}

	
	private void closeStream(Collection<List<InputStream>> streams) {
		if(!CollectionUtils.isEmpty(streams)) {
			streams.forEach(spiStreams -> {
				spiStreams.forEach(stream -> {
					try {
						stream.close();
					} catch (final IOException e) {
						LOGGER.warn("关闭spiStream 异常: {}",e.getMessage());
					}
				});
			});
			
			
		}
		
	}

	private void closeJarFile() {
		if(!CollectionUtils.isEmpty(JAR_FILES)) {
			JAR_FILES.forEach(jarFile -> {
				try {
					jarFile.close();
				} catch (final IOException e) {
					LOGGER.warn("关闭jarEntry资源异常:{}"+e.getMessage());
				}
			});
			JAR_FILES.clear();
		}
	}

	private void sortSPIMapper(final Map<Class<?>, List<SPIMapper>> spiMappers) {
		spiMappers.forEach((spiCls,spis) -> {
			Collections.sort(spis,(before,after) -> {
				final Integer beforeOrder = before.getOrder();
				final Integer afterOrder = after.getOrder();
				if(beforeOrder > afterOrder) {
					return 1;
				}else if(beforeOrder < afterOrder){
					return -1;
				}
				return 0;
			});
		});
		
	}
	

	private void getSPIMapperWithStream(final Map<String, List<InputStream>> streams,
			final Map<Class<?>, List<SPIMapper>> spiMappers) {
		final Set<Entry<String, List<InputStream>>> entrySet = streams.entrySet();
		entrySet.forEach(ent -> {
			Entry<String, List<InputStream>> entry = (Entry<String, List<InputStream>>) ent;
			final String spiClsName = entry.getKey();
			final List<InputStream> spiStreams = entry.getValue();
			if (!CollectionUtils.isEmpty(spiStreams)) {
				spiStreams.forEach(input -> {
					InputStream inputStream = (InputStream) input;
					final Properties define = PropertiesLoader.load(inputStream);
					bindSPI(define, spiClsName, spiMappers);
				});
			}
		});

	}

	public static Map<Class<?>, List<SPIMapper>> spis() {
		if (!LOADED.get()) {
			loading();
		}

		return Collections.unmodifiableMap(SPI_MAPPERS);
	}

	
	public static Set<String> spiName(final Class<?> spiCls){
		
		return null;
	}
	
	protected Enumeration<URL> getResources() throws IOException {
		final ClassLoader loader = SPILoader.class.getClassLoader();
		if (loader != null) {
			return loader.getResources(SPI_DIR);
		} else {
			return ClassLoader.getSystemResources(SPI_DIR);
		}

	}

	protected SPIResource getSPIResource(final Enumeration<URL> resources) throws URISyntaxException, IOException {
		if (resources != null) {
			final List<File> files = Lists.newArrayList();
			final Map<String, List<InputStream>> streams = Maps.newHashMap();
			while (resources.hasMoreElements()) {
				final URL url = resources.nextElement();
				if (!ResourceUtils.isJarURL(url)) {
					findSPIFiles(url, files);
				} else {
					findSPIFilesWithJar(url, streams);
				}
			}
			return SPIResource.create(files, streams);
		}
		return SPIResource.EMPTY;
	}

	private void findSPIFiles(final URL url, final List<File> files) throws URISyntaxException {
		final URI uri = url.toURI();
		final File file;

		try {
			file = new File(uri);
		} catch (final Throwable e) {
			LOGGER.error("无效的文件路径:{}", uri);
			return;
		}

		if (file.exists()) {
			final File[] spiFiles = file.listFiles(f -> {
				try {
					if (f.isDirectory()) {
						return false;
					}

					if (isSPIClass(f.getName())) {
						return true;
					}
					LOGGER.warn("非SPI文件定义,{}", f.getName());
					return false;
				} catch (ClassNotFoundException e) {
					LOGGER.warn("未找到SPI文件定义,{}", f.getName());
					return false;
				}
			});

			if (ArrayUtils.isNotEmpty(spiFiles)) {
				for (final File spiFile : spiFiles) {
					files.add(spiFile);
				}
			}
		}
	}

	private void findSPIFilesWithJar(URL url, final Map<String, List<InputStream>> streams)
			throws IOException, URISyntaxException {
		final JarFile jarFile = new JarFile(ResourceUtils.getFile(ResourceUtils.extractJarFileURL(url)));
		JAR_FILES.add(jarFile);
		final Enumeration<JarEntry> entries = jarFile.entries();

		while (entries.hasMoreElements()) {
			final JarEntry entry = entries.nextElement();
			final String fileName = entry.getName().replace("\\", "/");

			if (StringUtils.startsWith(fileName, SPI_DIR) && !entry.isDirectory()) {
				final String[] fileNameSection = fileName.split("/");
				final String spiFileName = fileNameSection[fileNameSection.length - 1];

				try {
					if (isSPIClass(spiFileName)) {
						if (!streams.containsKey(spiFileName)) {
							streams.put(spiFileName, Lists.newArrayList(jarFile.getInputStream(entry)));
						} else {
							streams.get(spiFileName).add(jarFile.getInputStream(entry));
						}
					} else {
						LOGGER.warn("非SPI文件定义,{}", spiFileName);
					}
				} catch (ClassNotFoundException e) {
					LOGGER.warn("未找到SPI文件定义: {}", spiFileName);
				}
			}
		}
	}

	private boolean isSPIClass(final String name) throws ClassNotFoundException {
		final Class<?> cls = Class.forName(name);
		if (cls.isAnnotationPresent(SPI.class)) {
			return true;
		}

		return false;
	}

	protected void getSPIMapper(final List<File> spiFiles, final Map<Class<?>, List<SPIMapper>> spiMappers) {
		if (!CollectionUtils.isEmpty(spiFiles)) {
			for (File spiFile : spiFiles) {
				final Properties define = PropertiesLoader.load(spiFile.getAbsolutePath());
				bindSPI(define, spiFile.getName(), spiMappers);
			}
		}

	}

	private void bindSPI(final Properties define, final String spiClsName,
			final Map<Class<?>, List<SPIMapper>> spiMappers) {
		final Class<?> spiCls = getSPIClass(spiClsName);
		if (spiCls == null) {
			return;
		}
		define.keySet().forEach(name -> {
			String spiName = (String) name;
			final String instanceClsName = define.getProperty(spiName);

			final Class<?> instanceCls;
			try {
				if (StringUtils.isNotBlank(instanceClsName)) {
					instanceCls = Class.forName(instanceClsName);
				} else {
					instanceCls = Class.forName(spiName);
					spiName = instanceCls.getSimpleName();
					LOGGER.debug("默认SPI定义: {} = {}", spiName, instanceCls.getName());
				}

				if (spiCls.isAssignableFrom(instanceCls)) {
					final SPIMapper spiMapper = SPIMapper.create(spiCls, spiName, instanceCls);
					if (!spiMappers.containsKey(spiCls)) {
						spiMappers.put(spiCls, Lists.newArrayList(spiMapper));
					} else {
						spiMappers.get(spiCls).add(spiMapper);
					}
				} else {
					LOGGER.warn("无法加载类 :{}, 未实现接口 : {}", instanceClsName, spiClsName);
				}

			} catch (ClassNotFoundException e) {
				LOGGER.warn("未定义SPI实现类: {} = {}", name, instanceClsName);
			}

		});

	}

	private Class<?> getSPIClass(String spiClsName) {
		try {
			return Class.forName(spiClsName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

}
