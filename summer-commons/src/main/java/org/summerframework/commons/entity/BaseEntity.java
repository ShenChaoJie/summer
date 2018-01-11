package org.summerframework.commons.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.summerframework.commons.format.ClassCast;
import org.summerframework.commons.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 基础实体类 , 实体功能扩展实体类
 * @author cjshen
 * @since 1.0
 */
public abstract class BaseEntity implements Cloneable, Serializable {

	private static final long serialVersionUID = -381153747554489443L;
	private static final transient List<String> FILTER_FIELD_NAME = Lists.newArrayList("names","cls","methods","fields");
	
	protected final transient Map<String,Method> methods = paramMethods(); //方法
	protected final transient Map<String,Field> fields = paramFields();  //属性
	
	private transient String[] names; //属性名称
	
	/**
	 * 获取所有的属性名称
	 * @return  数组
	 */
	public String[] attributeNames(){
		if(names != null){
			return names;
		}
		names = fields.keySet().toArray(new String[fields.size()]);
		
		return names;
	}
	
	/**
	 * 根据属性名获取该属性值 
	 * @param <T> 参数类型
	 * @param fieldName 属性名
	 * @return 该属性的值
	 */
	@SuppressWarnings("unchecked")
	public <T> T attributeValue(final String fieldName){
		if(StringUtils.isEmpty(fieldName)){
			throw new IllegalArgumentException("属性名称不能为空");
		}
		try {
			if(fields.containsKey(fieldName)){
				final Field field = fields.get(fieldName); //获取Filed 
				final String fieldGetName = parGetName(field.getName()); //获取 Field 的  getter()方法名称
				if(hasMethodName(fieldGetName)){ //判断是否有  getter方法
					return (T) methods.get(fieldGetName).invoke(this);
				}
			}else{
				throw new NoSuchFieldException("无效的属性名称: "+fieldName);
			}
			
		} catch (final Throwable e) {
			throw new EntityException(e.getMessage(), e);
		}
		
		return null;
	}
	
	 /**
     * 根据属性名获取该属性的值.
     * @param <T> 参数类型
     * @param fieldName 属性名
     * @param defaultValue 默认值，当field获取的值为null时选用defaulValue的值
     * @return 返回该属性的值
     */
	public <T> T attributeValue(final String fieldName,final T defaultValue){
		final T value = attributeValue(fieldName);
		return value == null ? defaultValue : value;
		
	}
	
	/**
     * 设置属性值.
     * @param fieldName 属性名
     * @param value 属性值
     * @param isCase 区分大小写，true时区分大小写，默认false
     */
	public void setAttributeValue(final String fieldName,final Object value,final boolean isCase){
		if(StringUtils.isEmpty(fieldName)){
			throw new IllegalArgumentException("属性名不能为空!");
		}
		try {
			 if(fields.containsKey(fieldName)){
				 final Field field = fields.get(fieldName);
				 if((!isCase && fieldName.toUpperCase().equals(field.getName().toUpperCase())) || (isCase && fieldName.equals(field.getName()))){
					 final String fieldSetName = parSetName(field.getName());
					 if(hasMethodName(fieldSetName)){
						 final String typeName = field.getType().getName();
						 methods.get(fieldSetName).invoke(this, ClassCast.cast(value, typeName));
					 }
				 }
			 }else{
				 throw new NoSuchFieldException("无效的属性名: " + fieldName);
			
			 }
		} catch (final Throwable e) {
			throw new EntityException(e.getMessage(),e);
		}
		
	}
	
	/**
	 * 设置属性
	 * @param fieldName 属性名
	 * @param value 属性值
	 */
	public void setAttributeValue(final String fieldName,final Object value){
		setAttributeValue(fieldName, value, false);//true时区分大小写，默认false 不区分大小写
	}
	
	
	/**
	 * 将 实体转化成 map
	 * @return
	 */
	public Map<String,Object> beanToMap(){
		final Map<String,Object> retMap = Maps.newHashMap();
		for(String key:attributeNames()){
			final Object  value = attributeValue(key);
			if(value != null){
				retMap.put(key, value);
			}
		}
		return retMap;
	}
	
	/**
	 * 将Map转化成实体对象
	 * @param <T> 参数类型
	 * @param beanMap  符合实体规范的map
	 * @param beanType 实体类型
	 * @return  转换后的实体类
	 */
	public static <T extends BaseEntity> T mapToBean(Map<String,Object> beanMap,Class<T> beanType){
		if(beanType == null){
			throw new EntityException("beanType不能为空");
		}
		if(beanMap == null){
			return null;
		}
		
		try {
			final T bean = beanType.newInstance();
			beanMap.forEach((key,value)->bean.setAttributeValue(key, value));
			return bean;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new EntityException(e.getMessage(),e);
		}
	}
	
	/**
	 * 将map对象集合 转成 实体对集合
	 * @param <T> 参数类型
	 * @param beanMaps 符合类型的map集合
	 * @param beanType 实体类型
	 * @return 转换后的实体类集合
	 */
	public static <T extends BaseEntity> List<T> mapToBeans(final List<Map<String,Object>> beanMaps,final Class<T> beanType){
		if(CollectionUtils.isEmpty(beanMaps)){
			return Collections.emptyList();
		}
		final List<T> beans = new ArrayList<T>(beanMaps.size());
		for(Map<String,Object> beanMap:beanMaps){
			beans.add(mapToBean(beanMap, beanType));
		}
		return beans;
	}
	
	
	
	
	
	/**
	 * 根据 属性名称 获取 getter()方法 名称
	 * get+属性名
	 * @param fieldName
	 * @return
	 */
	protected String parGetName(final String fieldName){
		if(StringUtils.isEmpty(fieldName)){
			return null;
		}
		return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	}
	
	
	/**
	 * 根据 属性名称 获取 setter()方法 名称
	 * set+属性名
	 * @param fieldName
	 * @return
	 */
	protected String parSetName(final String fieldName){
		if(StringUtils.isEmpty(fieldName)){
			return null;
		}
		return "set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
	}
	
	
	/**
	 * 查询是否有  该方法
	 * @param methodName
	 * @return
	 */
	private boolean hasMethodName(final String methodName){
		return methods.containsKey(methodName);
	}
	

	/**
	 * 获取实体类的所有属性
	 * @return 实体类属性列表
	 */
	public static Map<String,Field> paramFields(){
		return paramFields(Object.class);
	}
	
	
	/**
	 * 获取对象所有属性
	 * @param cls  对象类
	 * @return 实体类方法列表
	 */
	public static Map<String,Field> paramFields(Class<?> cls){
		final List<Field> fields = allFields(Lists.newArrayList(), cls);
		final Map<String,Field> fieldMap = new HashMap<String, Field>();
		
		for(Field field:fields ){
			if(Modifier.isFinal(field.getModifiers())||Modifier.isStatic(field.getModifiers())){//去掉 final 和  static 
				continue;
			}
			if(filterField(field)){//去掉 "names","cls","methods","fields"
				continue;
			}
			
			fieldMap.put(field.getName(), field);
		}
		
		return fieldMap;
	}
	
	/**
	 * 递归获取当前类和父类的所有属性
	 * @param allFields 实体类属性集合
	 * @param clazz 当前类 或 父类
	 * @return 新实体类属性集合
	 */
	protected static List<Field> allFields(final List<Field> allFields,final Class<?> clazz){
		allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		if(clazz.getSuperclass()==null){
			return allFields;
		}
		return allFields(allFields, clazz.getSuperclass());
	}
	
	/**
	 * 过滤 属性
	 * @param field
	 * @return
	 */
	protected static boolean filterField(final Field field){
		return FILTER_FIELD_NAME.contains(field.getName());
	}
	
	/**
	 * 获取实体类所有方法
	 * @return
	 */
	protected Map<String,Method> paramMethods(){
		
		return paramMethods(getClass());
		
	}
	
	/**
	 * 获取对象所有方法
	 * @param cls 对象类
	 * @return 实体类方法列表
	 */
	public static Map<String,Method> paramMethods(Class<?> cls){
		final List<Method> methods = allMethods(Lists.newArrayList(),cls);
		final Map<String,Method> methodMap = Maps.newHashMap();
		for(Method method:methods){
			if(Modifier.isFinal(method.getModifiers())|| Modifier.isStatic(method.getModifiers())){
				continue;
			}
			methodMap.put(method.getName(), method);
		}
		return methodMap;
	}
	
	
	/**
	 * 递归获取当前类 及 父类的所有方法
	 * @param allMethods 实体类方法集合
	 * @param clazz 当前类 和 父类
	 * @return  实体类类方法集合
	 */
	protected static List<Method> allMethods(final List<Method> allMethods,final Class<?> clazz){
		allMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		if(clazz.getSuperclass()==null){
			return allMethods;
		}
		return allMethods(allMethods,clazz.getSuperclass());
	}

	
	/**
	 * 获取实体类所有方法
	 * @return
	 */
    public Collection<Method> methods() {
        return methods.values();
    }
    
    /**
     * 获取实体类的所有属性
     * @return
     */
    public Collection<Field> fields() {
        return fields.values();
    }
    
    @Override
    public BaseEntity clone(){
		try {
			return (BaseEntity)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new EntityException("Clone Not Supported Exception"+e.getMessage());
		}
    }
    
    public String toString(){
		return JSON.toJSONString(this);
    }
    

}
