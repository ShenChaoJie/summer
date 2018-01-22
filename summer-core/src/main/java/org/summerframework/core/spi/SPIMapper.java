package org.summerframework.core.spi;

import org.summerframework.commons.entity.BaseEntity;

@SuppressWarnings({"rawtypes","unchecked"})
public class SPIMapper extends BaseEntity {

	private static final long serialVersionUID = -6217738658306105395L;
	private static final Integer DEFAULT_ORDER = 0;
	
	private final Class spi;
	private final String spiClsName;
	private final String name;
	private final Class instance;
	private final String instanceClsName;
	private final Boolean lazy;
	private final Integer order;
	
	private SPIMapper(final Class spi,final String name,final Class instance) {
		this.spi = spi;
		this.spiClsName = spi.getName();
		this.name = name;
		this.instance = instance;
		this.instanceClsName = instance.getName();
		this.lazy = instance.isAnnotationPresent(Lazy.class);
		
		if(instance.isAnnotationPresent(Order.class)) {
			final Order order = (Order) instance.getAnnotation(Order.class);
			this.order = order.value();
		}else {
			this.order = DEFAULT_ORDER;
		}
	}
	
	public static SPIMapper create(final Class spi,final String name,final Class instance) {
		return new SPIMapper(spi,name,instance);
	}
	
	
	public Class getSpi() {
		return spi;
	}
	public String getSpiClsName() {
		return spiClsName;
	}
	public String getName() {
		return name;
	}
	public Class getInstance() {
		return instance;
	}
	public String getInstanceClsName() {
		return instanceClsName;
	}
	public Boolean getLazy() {
		return lazy;
	}
	public Integer getOrder() {
		return order;
	}
	
	

}
