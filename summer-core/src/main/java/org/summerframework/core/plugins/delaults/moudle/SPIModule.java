package org.summerframework.core.plugins.delaults.moudle;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import com.google.inject.name.Names;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.core.globals.Globals;
import org.summerframework.core.plugins.Module;
import org.summerframework.core.spi.*;

import com.google.inject.Binder;
import com.google.inject.Injector;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class SPIModule implements Module {
	private static final Logger LOGGER = LoggerFactory.getLogger(SPIModule.class);

	
	@Override
	public void configure(Binder binder) {
		final Map<Class<?>,List<SPIMapper>> spiMappers = SPILoader.spis();
		if(!CollectionUtils.isEmpty(spiMappers)) {
			final Injector injector = Globals.get(Injector.class);
			spiMappers.forEach((spiCls,spis) -> {
				if(!spiCls.isAnnotationPresent(Lazy.class)) {
					 spis.forEach(spi -> {
						 final String spiClsName = spi.getSpiClsName();
						 final String name = spi.getName();
						 final String instanceClsName = spi.getInstanceClsName();
						 if (!spi.getLazy()) {
						 	final Object instance = injector.getInstance(spi.getInstance());
						 	binder.bind(spi.getSpi()).annotatedWith(Names.named(name)).toInstance(instance);
						 	LOGGER.debug("绑定即时SPI, 接口定义: {}, 绑定名称: {}, 实现类: {}", spiClsName, name, instanceClsName);
						 } else {
							binder.bind(spi.getSpi()).annotatedWith(Names.named(name)).toProvider(new SPIProvider(spi));
							LOGGER.debug("绑定延时SPI, 接口定义: {}, 绑定名称: {}, 实现类: {}", spiClsName, name, instanceClsName);
						 }
					 });
				}else {
					//写到这里 cjshen ,处理是@Lazy 子类  的延时绑定SPI

					
				}
				
			});
			
		}
		
	}

	@Override
	public List<Module> load() throws Throwable {
		return null;
	}

	@Override
	public void config(ServletConfig config) throws Throwable {
		
	}

	

}
