package org.summerframework.core.plugins;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.loader.PropertiesLoader;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.core.context.ApplicationContext;
import org.summerframework.core.globals.Globals;
import org.summerframework.core.plugins.delaults.moudle.SPIModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class PluginLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoader.class);
	
	protected ServletConfig config;
	protected ServletContext context;
	
	public void init(final HttpServlet servlet) {
		init(servlet.getServletConfig(),servlet.getServletContext());
	}
	
	public void init(final ServletConfig config) {
		init(config,null);
	}
	
	public void init(final ServletConfig config,final ServletContext context) {
		this.config = config;
		this.context = context;
		
		 initProperties();   //初始化配置文件
		 initRootInjector();   //初始化根注入
		 /*  initModules();        //初始化模块
         initPlugins();        //初始化插件
         initComponent();*/    //初始化组件
		
	}
	
	private void initProperties() {
		final long time = System.currentTimeMillis();
		
		try {
			final String context = config.getInitParameter(ApplicationContext.CONTEXT);
			if(StringUtils.isNotBlank(context)) {
				PropertiesLoader.load(context,true);
			}else {
				PropertiesLoader.load(ApplicationContext.MAIN_CONTEXT, true);
			}
		} catch (Throwable e) {
			throw new PluginLoaderException(e.getMessage(),e);
		}
		
		LOGGER.info("Loading the properties ,times: {}ms",System.currentTimeMillis()-time);
		
	}
	
	
	private void initRootInjector() {
		final Injector inject = Guice.createInjector();
		Globals.set(Injector.class, inject);
		Globals.set(Injector.class, inject.createChildInjector(new SPIModule()));
	}
	
	
	
	
	
	
	
	
	

}
