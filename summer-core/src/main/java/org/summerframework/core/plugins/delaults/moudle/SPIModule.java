package org.summerframework.core.plugins.delaults.moudle;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;

import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.core.globals.Globals;
import org.summerframework.core.plugins.Module;
import org.summerframework.core.spi.Lazy;
import org.summerframework.core.spi.SPI;
import org.summerframework.core.spi.SPILoader;
import org.summerframework.core.spi.SPIMapper;

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
						 //cjshen写到这里 , 没有反射方法
						 
						 
					 });
					
				}else {
					
					
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
