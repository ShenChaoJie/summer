package org.summerframework.commons.support.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.summerframework.commons.util.Assert;


public class Log4j2Impl extends AbstractAnalysisLogger implements org.summerframework.commons.support.logging.Logger {
	private static final String FQCN = Log4j2Impl.class.getName();
	private ExtendedLogger logger;
	
	public Log4j2Impl(final Logger logger) {
		Assert.notNull(logger);
		this.logger = (ExtendedLogger)logger;
		setLoggerName(logger.getName());
	}
	
	public Log4j2Impl(final String loggerName) {
		 Assert.hasText(loggerName);
		 this.logger = (ExtendedLogger) LogManager.getLogger(loggerName);
		 setLoggerName(loggerName);
	}
	

	@Override
	public boolean isErrorEnable() {
		return logger.isEnabled(Level.ERROR);
	}

	@Override
	public void error(String message, Throwable e) {
		logger.logIfEnabled(FQCN,Level.ERROR,null,message,e);
		incrementError();
	}

	@Override
	public void error(String message) {
		logger.logIfEnabled(FQCN,Level.ERROR,null,message);
		incrementError();
		
	}

	@Override
	public void error(String message, Object... args) {
		logger.logIfEnabled(FQCN,Level.ERROR,null,message,args);
		incrementError();
	}

	@Override
	public void error(Throwable cause) {
		logger.logIfEnabled(FQCN,Level.ERROR,null,cause.getMessage(),cause);
		incrementError();
	}

	@Override
	public boolean isWarnEnable() {
		return logger.isEnabled(Level.WARN);
	}

	@Override
	public void warn(String message) {
		logger.logIfEnabled(FQCN,Level.WARN,null,message);
		incrementWarn();
	}

	@Override
	public void warn(String message, Throwable e) {
		logger.logIfEnabled(FQCN,Level.WARN,null,message,e);
		incrementWarn();
	}

	@Override
	public void warn(String message, Object... args) {
		logger.logIfEnabled(FQCN,Level.WARN,null,message,args);
		incrementWarn();
	}

	@Override
	public void warn(Throwable cause) {
		logger.logIfEnabled(FQCN,Level.WARN,null,cause.getMessage(),cause);
		incrementWarn();
	}

	@Override
	public boolean isInfoEnable() {
		return  logger.isEnabled(Level.INFO);
	}

	@Override
	public void info(String message) {
		logger.logIfEnabled(FQCN,Level.INFO,null,message);
		incrementInfo();
	}

	@Override
	public void info(String message, Throwable e) {
		logger.logIfEnabled(FQCN,Level.INFO,null,message,e);
		incrementInfo();
	}

	@Override
	public void info(String message, Object... args) {
		logger.logIfEnabled(FQCN,Level.INFO,null,message,args);
		incrementInfo();
	}

	@Override
	public void info(Throwable cause) {
		logger.logIfEnabled(FQCN,Level.INFO,null,cause.getMessage(),cause);
		incrementInfo();
	}

	@Override
	public boolean isDebugEnable() {
		return logger.isEnabled(Level.DEBUG);
	}

	@Override
	public void debug(String message) {
		logger.logIfEnabled(FQCN,Level.DEBUG,null,message);
		incrementDebug();
	}

	@Override
	public void debug(String message, Throwable e) {
		logger.logIfEnabled(FQCN,Level.DEBUG,null,message,e);
		incrementDebug();
	}

	@Override
	public void debug(String message, Object... args) {
		logger.logIfEnabled(FQCN,Level.DEBUG,null,message,args);
		incrementDebug();
	}

	@Override
	public void debug(Throwable cause) {
		logger.logIfEnabled(FQCN,Level.DEBUG,null,cause.getMessage(),cause);
		incrementDebug();
	}

	@Override
	public boolean isTraceEnable() {
		return logger.isEnabled(Level.TRACE);
	}

	@Override
	public void trace(String message) {
		logger.logIfEnabled(FQCN,Level.TRACE,null,message);
		incrementTrace();
	}

	@Override
	public void trace(String message, Throwable e) {
		logger.logIfEnabled(FQCN,Level.TRACE,null,message,e);
		incrementTrace();
	}

	@Override
	public void trace(String message, Object... args) {
		logger.logIfEnabled(FQCN,Level.TRACE,null,message,args);
		incrementTrace();
	}

	@Override
	public void trace(Throwable cause) {
		logger.logIfEnabled(FQCN,Level.TRACE,null,cause.getMessage(),cause);
		incrementTrace();
	}

}
