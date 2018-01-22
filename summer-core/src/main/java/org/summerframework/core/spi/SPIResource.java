package org.summerframework.core.spi;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.summerframework.commons.entity.BaseEntity;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public class SPIResource extends BaseEntity {
	public static final SPIResource EMPTY = SPIResource.create(Collections.emptyList(), Collections.emptyMap());
	private static final long serialVersionUID = 661818265825491286L;
	
	private final List<File> files;
	private final Map<String,List<InputStream>> streams;
	
	private SPIResource(final List<File> files,Map<String,List<InputStream>> streams) {
		this.files = files;
		this.streams = streams;
	}

	public List<File> getFiles() {
		return files;
	}

	public Map<String, List<InputStream>> getStreams() {
		return streams;
	}
	
	public static SPIResource create(final List<File> files,Map<String,List<InputStream>> streams) {
		return new SPIResource(files,streams);
	}
	

}
