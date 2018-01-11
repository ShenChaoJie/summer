package org.summerframework.commons.util;

import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 对象比较处理类
 * @author cjshen
 * @since 1.0
 */
public class ObjectCompare {
	
	/**
	 * 判断目标值是否存在源列表中
	 * @param target 目标
	 * @param sources 源列表
	 * @return 返回是否存在  true=存在 ,false=不存在
	 */
	public static final boolean isInList(Object target,Object... sources){
		if(target == null)
			return false;
		
		if(sources != null && sources.length > 0){
			for(Object src:sources){
				if(target.equals(src)){
					return true;
				}
				
			}
		}
		return false;
	}
	
	/**
	 * 判断目标值是否存在源列表中
	 * @param target 目标
	 * @param sources 源列表
	 * @return 返回是否存在  true=存在 ,false=不存在
	 */
	public static final boolean isInList(Object target,String... sources){
		if(target == null)
			return false;
		if(sources != null && sources.length > 0){
			for(String src:sources){
				if(StringUtils.isEmpty(src)){
					continue;
				}
				
				if(target.equals(src.trim())){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 正则表达式 比较 target 是否在 regExs 内
	 * @param target 目标
	 * @param regExs 正则表达式列表
	 * @return 返回是否存在结果 true=存在，false=不存在
	 */
	public static final boolean isInListByRegEx(String target , String... regExs ){
		if(target == null){
			return false;
		}
		if(regExs != null && regExs.length > 0){
			for(String regEx:regExs){
				if(StringUtils.isBlank(regEx)){
					continue;
				}
				if(Pattern.matches(regEx, target)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 *  正则表达式 比较 target 是否在 regExs 内
	 * @param target 目标
	 * @param regExs  Set去重的正则表达式列表
	 * @return 返回是否存在结果 true=存在，false=不存在
	 */
	public static final boolean isInListByRegEx(String target , Set<String> regExs){
		if(CollectionUtils.isEmpty(regExs)){
			return false;
		}
		
		return isInListByRegEx(target,regExs.toArray(new String[regExs.size()]));
	}
	
	/**
	 * 判断目标值 后缀 是否存在与 源列表
	 * @param target 目标
	 * @param sources 源列表
	 * @return  返回是否存在结果 true=存在，false=不存在
	 */
	public static final boolean isInEndWiths(String target,String... sources){
		if(target == null){
			return false;
		}
		if(sources != null && sources.length > 0){
			for(String suffix:sources){
				if(target.endsWith(suffix)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	

}
