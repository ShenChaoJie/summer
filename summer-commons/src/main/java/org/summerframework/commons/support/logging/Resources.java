package org.summerframework.commons.support.logging;

public final class Resources {

	private static ClassLoader defaultClassLoader;
	
	public Resources(){
		
	}

	public static ClassLoader getDefaultClassLoader() {
		return defaultClassLoader;
	}

	public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		Resources.defaultClassLoader = defaultClassLoader;
	}
	
	public static Class<?> classForName(String className) throws ClassNotFoundException{
		Class<?> clazz = getClassLoader().loadClass(className);
		if(clazz == null) {
			clazz = Class.forName(className);
		}
		return clazz;
	}
	
	private static ClassLoader getClassLoader() {
		if(defaultClassLoader!=null) {
			return defaultClassLoader;
		}else {
			return Thread.currentThread().getContextClassLoader();
		}
	}
	
}
