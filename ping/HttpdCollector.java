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
import io.prometheus.client.Collector.MetricFamilySamples;

public class HttpdCollector extends Collector {

	private static final int SUCCESS = 1;
	private static final int FAIL = 0;
	
	// default values
	private String httpd_subsystem1;
	private String httpd_url1;
	private String httpd_match1;
	private String httpd_metricname;
	private String httpd_labelname;
	
	public HttpdCollector() {
		// TODO Auto-generated constructor stub
	}

	public HttpdCollector(String httpd_subsystem1, String httpd_url1, 
		String httpd_match1, String httpd_metricname, String httpd_labelname) {
		
		this.httpd_subsystem1 = httpd_subsystem1;
		this.httpd_url1 = httpd_url1;
		this.httpd_match1 = httpd_match1;
		this.httpd_metricname = httpd_metricname;
		this.httpd_labelname = httpd_labelname;
	}

	
	@Override
	public List<MetricFamilySamples> collect() {
		
	       List<MetricFamilySamples> mfs = new ArrayList<MetricFamilySamples>();
	       
	       // run the healthchecks
	       int rc1 = checkServletHealth(httpd_url1, httpd_match1);
	       //int rc2 = checkServletHealth(servleturl2, match2);
	       
	       // With no labels.
	       //mfs.add(new GaugeMetricFamily(metricname, "help for " + metricname, rc));
	       
	       // With labels
		
	       GaugeMetricFamily labeledGauge = new GaugeMetricFamily(httpd_metricname,
	    		"help", Arrays.asList(httpd_labelname));
	       labeledGauge.addMetric(Arrays.asList(httpd_subsystem1), rc1);
	       //labeledGauge.addMetric(Arrays.asList(subsystem2), rc2);
	       mfs.add(labeledGauge);
			 
	       return mfs;
		}
		
		private int checkServletHealth(String addressurl, String match) {
			
			int rc; 
			URL url;
			
			try {
				url = new URL( addressurl );
			
		        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream())); 
		        String line = in.readLine(); 
		        
		        if (line.indexOf(match) > -1) {
		        	
		        	System.out.println( new Date() + " HTTPD SUCCESS - MATCH FOUND = " + line );
		        	rc = SUCCESS;
		        	
		        } else {
		        	
		        	System.out.println(new Date() + " HTTPD FAIL - " + line ); 
		        	rc = FAIL;
		        }
		        
		        in.close();
		        return rc;
		        
			} catch ( IOException e) {
				System.out.println(new Date() + " HTTPD FAIL - ERROR pinging servlet : " + e.getMessage());
				//e.printStackTrace();
				return FAIL;
			} 
		}

}
