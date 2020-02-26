package com.generali.ping;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;

// @see https://hellokoding.com/java-application-health-check-with-prometheus-grafana-mysql-and-docker-compose/
// 
// export the healthcheck of a servlet to prometheus 

public class HealthExporter {

	private  ServletCollector servletCollector = null;
	private  HttpdCollector httpdCollector = null;

    public HealthExporter(ServletCollector servletCollector) {
        this.servletCollector = servletCollector;
    }
    
    public HealthExporter(ServletCollector servletCollector, 
    					   HttpdCollector httpdCollector) {
        this.servletCollector = servletCollector;
        this.httpdCollector = httpdCollector;
    }
    
    public void export() throws Exception {
    	
    	if (this.servletCollector != null) CollectorRegistry.defaultRegistry.register(this.servletCollector);
        if (this.httpdCollector != null) CollectorRegistry.defaultRegistry.register(this.httpdCollector);

        // Expose metrics to port 8081 by running a Jetty server
        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new MetricsServlet()), "/metrics");
        DefaultExports.initialize();

        server.start();
        server.join();
        
    }

}
