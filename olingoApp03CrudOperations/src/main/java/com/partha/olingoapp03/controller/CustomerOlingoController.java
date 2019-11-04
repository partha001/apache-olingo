package com.partha.olingoapp03.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.partha.olingoapp03.processors.MyEntityCollectionProcessor;
import com.partha.olingoapp03.processors.MyEntityProcessor;
import com.partha.olingoapp03.providers.MyEdmProvider;

public class CustomerOlingoController extends HttpServlet{
	
private static final long serialVersionUID = 1L;
	
	Logger logger = LoggerFactory.getLogger(CustomerOlingoController.class);
	
	private MyEdmProvider edmProvider;
	private MyEntityCollectionProcessor entitySetProcessor;
	private MyEntityProcessor entityProcessor;
	
	public CustomerOlingoController() {}
	
	public CustomerOlingoController(MyEdmProvider edmProvider, 
			MyEntityCollectionProcessor entitySetProcessor,
			MyEntityProcessor entityProcessor) {
		this.edmProvider = edmProvider;
		this.entitySetProcessor = entitySetProcessor;
		this.entityProcessor = entityProcessor;
	}

	public void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
		
		try {
			logger.info("Odata request service started");
			OData odata = OData.newInstance();		
			ServiceMetadata edm = odata.createServiceMetadata(edmProvider, new ArrayList<EdmxReference>());
			ODataHttpHandler handler = odata.createHandler(edm);
			handler.register(entitySetProcessor);
			handler.register(entityProcessor);
			handler.process(servletRequest, servletResponse);
			logger.info("Proccess completed");
		} catch (RuntimeException e) {
			logger.error("Exception occurred while processing");
			throw new ServletException(e);
		}
	}

}
