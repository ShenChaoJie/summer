package org.summerframework.core.spi;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class SPIException extends RuntimeException {

	private static final long serialVersionUID = -3921161014233480139L;

	public SPIException(final String message) {
		super(message);
	}
	
	public SPIException(final String message,final Throwable e) {
		super(message,e);
	}
	
	
}
