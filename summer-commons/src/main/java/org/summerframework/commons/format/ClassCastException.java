package org.summerframework.commons.format;

public class ClassCastException extends RuntimeException {

	private static final long serialVersionUID = -673370404050709478L;
	
	
	public ClassCastException(final String message,Throwable cause){
		super(message, cause);
	}
	
	public ClassCastException(final String message){
		super(message);
	}
	
	@Override
	public String getMessage(){
		return "加载异常:"+super.getMessage();
	}

}
