package org.summerframework.commons.entity;

/**
 * 实体操作异常类
 * @author cjshen
 *
 */
public class EntityException extends RuntimeException {
	private static final long serialVersionUID = 7606794113293775197L;
	
	/**
	 * 
	 * @param message
	 */
	public EntityException(final String message){
		super(message);
	}
	
	/**
	 * 
	 * @param message
	 * @param cause  错误对象
	 */
	public EntityException(final String message,final Throwable cause){
		super(message, cause);
	}
	
	public String getMessage(){
		return "实体操作异常: " + super.getMessage();
	}
	

}
