package com.generali.ping;

// start the HealthExporter

public class HealthAgent {
	
	// servlet collector params
	private static String subsystem1 = "abcdef01";
	private static String servleturl1 = "http://localhost:8180/JBossWebPingBackend/PingServlet1";
	private static String subsystem2 = "abcdef02";
	private static String servleturl2 = "http://localhost:8180/JBossWebPingBackend/PingServlet2";
	private static String match1 = "PingServlet1 served at: /JBossWebPingBackend";
	private static String match2 = "PingServlet2 served at: /JBossWebPingBackend";
	private static String metricname = "ping_JBOSS_gauge";
	private static String labelname = "AppServer";
	
	// httpd collector params
	private static String httpdsubsystem1 = "websrv01";
	private static String httpd_url1 = "http://localhost:8080";
	private static String httpd_match1 = "It works!";
	private static String httpd_metricname = "ping_HTTPD_gauge";
	private static String httpdlabelname = "HttpdServer";

	public static void main(String[] args) {
		
		// create the servlet collector
		ServletCollector servletCollector = new ServletCollector(subsystem1, servleturl1, subsystem2, 
				servleturl2, match1, match2, 
				metricname, labelname);
		
		// create the httpd collector
		HttpdCollector httpdCollector = new HttpdCollector(httpdsubsystem1, httpd_url1,
				httpd_match1, httpd_metricname, httpdlabelname);
				
		HealthExporter exporter = new HealthExporter(servletCollector, httpdCollector);
		
		try {
			// Launch the exporter
			exporter.export();
			//System.out.println("Exporter launched");
			
		} catch (Exception e) {
			System.out.println("ERROR launching exporter " + e.getLocalizedMessage());
			
			e.printStackTrace();
		}
		

	}

}
