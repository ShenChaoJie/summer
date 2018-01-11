package org.summerframework.commons.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 时间格式转换类
 * @author cjshen
 * @since 1.0
 */
public final class DateFormat {
	
	private static final Map<String,SimpleDateFormat> FORMAT_MAP = Maps.newConcurrentMap();
	
	private DateFormat(){
		
	}
	
	/**
	 * 获取时间转换对象
	 * @param pattern 时间格式
	 * @return  SimpleDateFormat
	 */
	public static final SimpleDateFormat get(final String pattern){
		SimpleDateFormat format;
		if((format=FORMAT_MAP.get(pattern))==null){
			format = new SimpleDateFormat(pattern);
			FORMAT_MAP.put(pattern, format);
		}
		return format;
	}
	
	/**
	 *  获取时间转换对象
	 * @param pattern 时间格式 枚举
	 * @return SimpleDateFormat
	 */
	public static final SimpleDateFormat get(final Pattern pattern){
		//Assert.isNull(pattern,"pattern must be not null.");
		return get(pattern.get());
	}
	
	/**
	 * 将时间对象转换成 时间格式字符串
	 * @param date 时间对象
	 * @param pattern 时间格式枚举
	 * @return 时间格式字符串
	 */
	public static final String format(final Date date,final Pattern pattern){
		return get(pattern).format(date);
	}
	
	/**
	 * 将时间对象转换成 时间格式字符串
	 * @param date 时间对象
	 * @param pattern 时间格式
	 * @return 时间格式字符串
	 */
	public static final String fromat(final Date date,final String pattern){
		return get(pattern).format(date);
	}
	
	/**
	 * 将时间对象转换成 时间格式字符串
	 * @param date 时间对象
	 * @param pattern 时间格式枚举
	 * @return 时间格式字符串
	 */
	public static final String format(final Object date,final Pattern pattern){
		return get(pattern).format(date);
	}
	

	/**
	 * 将时间对象转换成 时间格式字符串
	 * @param date 时间对象
	 * @param pattern 时间格式
	 * @return 时间格式字符串
	 */
	public static final String format(final Object date,final String pattern){
		return get(pattern).format(date);
	}
	
	/**
	 * 将时间类型字符串转化成时间对象
	 * @param <T> 参数类型
	 * @param date 时间类型字符串
	 * @param pattern 时间格式枚举
	 * @return 时间对象
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Date> T parse(final String date,final Pattern pattern) throws ParseException{
		return (T)get(pattern).parse(date);
	}
	
	
	/**
	 * 将时间类型字符串转化成时间对象
	 * @param <T> 参数类型
	 * @param date 时间类型字符串
	 * @param pattern 时间格式
	 * @return 时间对象
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends Date> T parse(final String date,final String pattern) throws ParseException{
		return (T)get(pattern).parse(date);
	}
	

}
