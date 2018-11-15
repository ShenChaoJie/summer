package org.summerframework.core.globals;

import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

/**
 * 全局变量,针对一些全局的属性统一管理
 * @author cjshen
 * @since 1.0
 */
public final class Globals {
	private static final ConcurrentMap<Class<?>, Object> GLOBALS = Maps.newConcurrentMap();
	
	public Globals() {
		
	}
	
	public static void set(final Class<?> clz,final Object global) {
		GLOBALS.put(clz, global);
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T get(final Class<T> clz) {
		return (T) GLOBALS.get(clz);
	}
	
	
}
