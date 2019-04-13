package org.summerframework.core.plugins;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.loader.PropertiesLoader;
import org.summerframework.commons.support.logging.Logger;
import org.summerframework.commons.support.logging.LoggerFactory;
import org.summerframework.commons.util.CollectionUtils;
import org.summerframework.core.context.ApplicationContext;
import org.summerframework.core.globals.Globals;
import org.summerframework.core.plugins.delaults.moudle.SPIModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.summerframework.core.spi.Level;
import org.summerframework.core.spi.SPILoader;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组件初始化.
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
		try {
		 	initProperties();   //初始化配置文件
		 	initRootInjector();   //初始化根注入
		 	initModules();       //初始化模块
         	initPlugins();        //初始化插件

         	initComponent();    //初始化组件
		} catch (final Throwable e) {
			throw new PluginLoaderException(e.getMessage(), e);
		}

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


	private void initModules() throws Throwable{
		final Set<String> moduleNames = SPILoader.spiNames(Module.class);
		if(!CollectionUtils.isEmpty(moduleNames)){
			final Injector injector = Globals.get(Injector.class);
			final Map<Integer, List<Module>> modules = Maps.newHashMap();
			for (final String moduleName : moduleNames){
				final Module module = injector.getInstance(Key.get(Module.class, Names.named(moduleName)));
				final Level level = module.getClass().getAnnotation(Level.class);
				if(level != null){
					addModules(modules, level.value(), module);
				}else{
					addModules(modules, 0 ,module);
				}
			}
            loadModules(modules);
		}
	}


	private void addModules(final Map<Integer, List<Module>> modules, final Integer level, final Module module){
		if(modules.containsKey(level)){
			modules.get(level).add(module);
		} else {
			modules.put(level, Lists.newArrayList(module));
		}
	}


	private void loadModules(final  Map<Integer, List<Module>> loadingModules) throws Throwable{
	    final List<Integer> levels = Lists.newArrayList();
	    loadingModules.keySet().forEach(level -> levels.add(level));
	    Collections.sort(levels);
	    for(final Integer level : levels){
	        final List<Module> modules = loadingModules.get(level);
	        if(!CollectionUtils.isEmpty(modules)){
	            final List<Module> mdus = Lists.newArrayList();
	            for(final Module module : modules){
                    module.config(config);
                    mdus.addAll(module.load());
                }

                if(!CollectionUtils.isEmpty(mdus)){
                    if(level.intValue() == 0){
                        mdus.add(0, new SPIModule());
                        Globals.set(Injector.class, Guice.createInjector(mdus));
                    } else {
                        Globals.set(Injector.class, Globals.get(Injector.class).createChildInjector(mdus));
                    }
                }


            }

        }

    }


    private void initPlugins() throws Throwable{
	    final Set<String> pluginNames = SPILoader.spiNames(Plugin.class);
        if(!CollectionUtils.isEmpty(pluginNames)){
            final Injector injector = Globals.get(Injector.class);
            for (final String pluginName : pluginNames){
                final Plugin plugin = injector.getInstance(Key.get(Plugin.class, Names.named(pluginName)));
                plugin.config(config);
                if (plugin.load()){
                    LOGGER.info("Loading Plugin: {}", plugin.getClass().getName());
                }
            }

        }


    }

    private void initComponent() throws Throwable{
		final long time = System.currentTimeMillis();
		LOGGER.info("Starting inject component");
		//Components.load();
		//写到这里
		LOGGER.info("Inject Compent complete, times: {}ms", System.currentTimeMillis() - time);


	}
	
	
	
	

}
