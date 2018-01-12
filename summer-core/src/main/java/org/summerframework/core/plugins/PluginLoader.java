package org.summerframework.core.plugins;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;

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
		
		//cjshen 写到这里
		 /*initProperties();   //初始化配置文件
         initRootInjector();   //初始化根注入
         initModules();        //初始化模块
         initPlugins();        //初始化插件
         initComponent();*/    //初始化组件
		
	}
	
	

}
