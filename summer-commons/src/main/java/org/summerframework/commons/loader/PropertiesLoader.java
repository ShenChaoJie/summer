package org.summerframework.commons.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.io.ClassPathResource;
import org.summerframework.commons.io.Resource;
import org.summerframework.commons.util.ResourceUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

/**
 * 属性文件操作公有类，负责对属性文件进行读写操作.
 * @author cjshen
 * @since 1.0
 */
public final class PropertiesLoader {
	
	/**
	 * 属性文件集合
	 */
	public static final Map<String,Properties> PROPERTIES = Maps.newHashMap();
	
	/**
	 * 属性配置列表根
	 */
	public static final String CONTEXT = "context";
	
	
	public PropertiesLoader() {
		
	}

	/**
	 * 根据路径加载配置文件
	 * @param path
	 * @return
	 */
	public static Properties load(final String path){
		try {
			InputStream input = null;
			try {
				final Resource recource = new ClassPathResource(path);
				input = recource.getInputStream();
			} catch (IOException e) {
				// ignore
			}
			
			final Properties properties;
			if(input != null){
				properties = load(input);
			}else{
				properties = load(ResourceUtils.getFile(path));
			}
			return properties;
		} catch (IOException e) {
			 throw new LoaderException("加载属性文件异常: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 通过输入流加载文件
	 * @param input
	 * @return
	 * @throws IOException 
	 */
	public static Properties load(final InputStream input) throws IOException{
		if(input == null){
			 throw new LoaderException("输入流为空");
		}
		
		final Properties properties = new Properties();
		properties.load(new InputStreamReader(input,Charsets.UTF_8));
		return properties;
	}
	
	/**
	 * 通过文件加载属性文件
	 * @param file
	 * @return 返回加载后的Properties
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static Properties load(final File file) throws FileNotFoundException, IOException{
		if(file == null){
			throw new LoaderException("文件对象为空");
		}
		final Properties prop = new Properties();
		prop.load(new InputStreamReader(new FileInputStream(file),Charsets.UTF_8));
		return prop;
	}
	
	/**
	 * 加载属性文件
	 * @param ContextPath  文件相对路径
	 * @param loadContext  是否加载context
	 */
	public static void load(final String contextPath,final boolean loadContext){
		final Properties prop = load(contextPath);//根据路径加载配置文件
		prop.forEach((key,value)->System.setProperty((String)key, (String)value));//将properties设置成 系统的全局变量
		PROPERTIES.put(contextPath, prop);
		
		if(loadContext){
			final String context = prop.getProperty(CONTEXT);
			if(StringUtils.isNotEmpty(context)){
				final String[] ctxs = context.split(";");
				if(ctxs.length>0){
					for(String ctx:ctxs){
						if(StringUtils.isNotBlank(ctx)){
							final Properties properties = load(ctx);
							if(properties != null){
								PROPERTIES.put(ctx, properties);
							}else{
								//LOGGER.error(ctx + ": 无法加载此属性文件!");
							}
						}
					}
				}
			}
			
		}
		
		
		
	}
	
	
	
	
	

}
