package org.summerframework.commons.loader;

/**
 * 加载异常处理类
 * @author cjshen
 * @since 1.0
 */
public class LoaderException extends RuntimeException {

	private static final long serialVersionUID = -516296896332449075L;
	
	
	public LoaderException(final String message){
		super(message);
	}

	public LoaderException(final String message,final Throwable cause){
		super(message,cause);
	}
	
	public String getMessage(){
		return "加载类异常: "+super.getMessage();
	}
	
}
