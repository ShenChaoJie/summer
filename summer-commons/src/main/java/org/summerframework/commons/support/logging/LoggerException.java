package org.summerframework.commons.support.logging;

public class LoggerException extends RuntimeException {

	private static final long serialVersionUID = 3348778823380442844L;
	
	public LoggerException(final String message) {
		super(message);
	}
	
	public LoggerException(final String message,final Throwable cause) {
		super(message,cause);
	}
	

}
