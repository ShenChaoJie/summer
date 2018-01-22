package org.summerframework.core.plugins;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class PluginLoaderException extends RuntimeException {

	private static final long serialVersionUID = 8883994705320203635L;
	
	public PluginLoaderException() {
		super();
	}

	public PluginLoaderException(String message) {
		super(message);
	}
	
	public PluginLoaderException(String message,Throwable cause) {
		super(message,cause);
	}
	
	public PluginLoaderException(Throwable cause) {
		super(cause);
	}
	
}
