package org.summerframework.commons.support.logging;


/**
 * 
 * @author cjshen
 * @since 1.0
 */
public interface Logger {
	
	boolean isErrorEnable();
	
	void error(String message , Throwable e);
	
	void error(String message);
	
	void error(String message , Object... args);

	void error(Throwable cause);
	
	boolean isWarnEnable();
	
	void warn(String message);
	
	void warn(String message,Throwable e);
	
	void warn(String message,Object... args);
	
	void warn(Throwable cause);
	
	boolean isInfoEnable();
	
	void info(String message);
	
	void info(String message,Throwable e);
	
	void info(String message , Object... args);
	
	void info(Throwable cause);
	
	boolean isDebugEnable();
	
	void debug(String message);
	
	void debug(String message,Throwable e);
	
	void debug(String message,Object... args);

	void debug(Throwable cause);
	
	boolean isTraceEnable();
	
	void trace(String message);
	
	void trace(String message,Throwable e);
	
	void trace(String message,Object... args);
	
	void trace(Throwable cause);
	
	
	
}

