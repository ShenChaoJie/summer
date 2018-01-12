package org.summerframework.commons.support.logging;

import java.lang.reflect.Constructor;

/**
 * 日志工厂类
 * @author cjshen
 * @since 1.0
 */
public class LoggerFactory {
	
	public static final String LOG4J2_LOGGER = "org.apache.logging.log4j.Logger";
	
	public static final String LOG4J2_IMPL_CLASS_NAME = "org.summerframework.commons.support.logging.Log4j2Impl";
	
	private static Constructor<?> LOGGER_CONSTRUCTOR;//反射构造器
	
	static {
		tryImplementation(LOG4J2_LOGGER, LOG4J2_IMPL_CLASS_NAME);
		//tryNoLoggingImplementation();
	}
	
	private static synchronized void tryImplementation(final String testClassName,final String implClassName) {
		if(LOGGER_CONSTRUCTOR != null) {
			return;
		}
		try {
			Resources.classForName(testClassName);
			final Class<?> implClass = Resources.classForName(implClassName);
			LOGGER_CONSTRUCTOR = implClass.getConstructor(new Class[] {String.class});
			
			final Class<?> declareClass = LOGGER_CONSTRUCTOR.getDeclaringClass();
			if(!Logger.class.isAssignableFrom(declareClass)) {
				LOGGER_CONSTRUCTOR = null;
			}
			
			 try {
				 if(null == LOGGER_CONSTRUCTOR) {
					 LOGGER_CONSTRUCTOR.newInstance(LoggerFactory.class.getName());
				 }
				
			} catch (final Throwable e) {
				LOGGER_CONSTRUCTOR = null;
			}
			
		} catch (Throwable t) {
			//ignore
		}
		
	}
	
	
	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz.getName());
	}
	
	public static Logger getLogger(final String loggerName) {
		try {
			return (Logger) LOGGER_CONSTRUCTOR.newInstance(loggerName);
		} catch (Throwable e) {
			throw new LoggerException(e.getMessage(),e);
		}
	}	

}
