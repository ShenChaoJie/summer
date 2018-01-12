package org.summerframework.commons.support.logging;

import java.util.Set;

/**
 * 
 * @author cjshen
 * @since 1.0
 */
public interface AnalysisLoggerMXBean {
	
	 String OBJECT_NAME = "org.summerframework:type=AnalysisLogger";
	
	Set<String> getErrorkeys();
	
	Set<String> getWarnkeys();
	
	Set<String> getInfokeys();
	
	Set<String> getDebugkeys();

	Set<String> getTraceKeys();
	
	long getErrorCount();
	
	long getWarnCount();
	    
    long getInfoCount();
    
    long getDebugCount();
    
    long getTraceCount();
    
    void reset();
    
    long getErrorTotal();
    
    long getWarnTotal();
    
    long getInfoTotal();
    
    long getDebugTotal();
    
    long getTraceTotal();
    
    void resetAll();
	
}
