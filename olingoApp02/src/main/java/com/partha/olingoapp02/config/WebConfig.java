package com.partha.olingoapp02.config;

import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.partha.olingoapp02.controller.CustomerOlingoController;
import com.partha.olingoapp02.processors.MyEntityCollectionProcessor;
import com.partha.olingoapp02.providers.MyEdmProvider;


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
