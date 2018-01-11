package org.summerframework.commons.format;

/**
 * 时间格式
 * @author cjshen
 * @since 1.0
 */
public enum Pattern {
	
	DATE("yyyy-MM-dd"),
	
	TIME("HH:mm:ss"),

	DATETIME("yyyy-MM-dd HH:mm:ss"),
	
	TIMESTAMP("yyyy-MM-dd HH:mm:ss.SSS");
	
	private String pattern;

	Pattern(final String pattern) {
		this.pattern = pattern;
	}

	public String get(){
		return pattern;
	}
	
}
