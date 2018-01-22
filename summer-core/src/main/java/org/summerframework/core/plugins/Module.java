package org.summerframework.core.plugins;

import java.util.List;

import javax.servlet.ServletConfig;

import org.summerframework.core.spi.Lazy;
import org.summerframework.core.spi.SPI;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
@SPI
@Lazy
public interface Module extends com.google.inject.Module {
	
	List<Module> load() throws Throwable;
	
	void config(ServletConfig config) throws Throwable;

}
