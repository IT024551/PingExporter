package com.generali.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

public class ServletCollector extends Collector {

	private static final int SUCCESS = 1;
	private static final int FAIL = 0;
	
	// default values
	private String subsystem1;
	private String servleturl1;
	private String subsystem2;
	private String servleturl2;
	private String match1;
	private String match2;
	private String metricname;
	private String labelname;
	
	public ServletCollector() {
		// TODO Auto-generated constructor stub
	}
	
	public ServletCollector(String subsystem1, String servleturl1, String subsystem2, 
							String servleturl2, String match1, String match2, 
							String metricname, String labelname) {
		//
		this.subsystem1 = subsystem1;
		this.servleturl1 = servleturl1;
		this.subsystem2 = subsystem2;
		this.servleturl2 = servleturl2;
		this.match1 = match1;
		this.match2 = match2;
		this.metricname = metricname;
		this.labelname = labelname;
	}
	
	public List<MetricFamilySamples> collect() {
		
       List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
       
       // run the healthchecks
       int rc1 = checkServletHealth(servleturl1, match1);
       int rc2 = checkServletHealth(servleturl2, match2);
       
       // With no labels.
       //mfs.add(new GaugeMetricFamily(metricname, "help for " + metricname, rc));
       
       // With labels
	
       GaugeMetricFamily labeledGauge = new GaugeMetricFamily(metricname,
    		"help", Arrays.asList(labelname));
       labeledGauge.addMetric(Arrays.asList(subsystem1), rc1);
       labeledGauge.addMetric(Arrays.asList(subsystem2), rc2);
       mfs.add(labeledGauge);
		 
       return mfs;
	}
	
	private int checkServletHealth(String servleturl, String match) {
		
		int rc; 
		URL url;
		
		try {
			url = new URL( servleturl );
		
	        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
	        String line = in.readLine(); 
	        
	        if (line.indexOf(match) > -1) {
	        	
	        	System.out.println( new Date() + " SUCCESS - MATCH FOUND = " + line );
	        	rc = SUCCESS;
	        	
	        } else {
	        	
	        	System.out.println(new Date() + " FAIL - " + line ); 
	        	rc = FAIL;
	        }
	        
	        in.close();
	        return rc;
	        
		} catch ( IOException e) {
			System.out.println(new Date() + " FAIL - ERROR pinging servlet : " + e.getMessage());
			//e.printStackTrace();
			return FAIL;
		} 
	}

}
