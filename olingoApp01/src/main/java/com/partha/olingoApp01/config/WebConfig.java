package com.partha.olingoApp01.config;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.partha.olingoApp01.controller.CustomerOlingoController;
import com.partha.olingoApp01.processors.MyEntityCollectionProcessor;
import com.partha.olingoApp01.providers.MyEdmProvider;



@Configuration
public class WebConfig {
	
	@Autowired
	private MyEdmProvider edmProvider;
	
	@Autowired
	private MyEntityCollectionProcessor entitySetProcessor;

	 @Bean	
	   public ServletRegistrationBean<HttpServlet> countryServlet() {
		   ServletRegistrationBean<HttpServlet> servRegBean = new ServletRegistrationBean<>();
		   servRegBean.setServlet(new CustomerOlingoController(this.edmProvider,this.entitySetProcessor));
		   servRegBean.addUrlMappings("/myapp.svc/*");
		   servRegBean.setLoadOnStartup(1);
		   return servRegBean;
	   }
	
}
