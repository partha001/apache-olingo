package com.partha.olingoapp03.processors;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.partha.olingoapp03.providers.MyEdmProvider;

@Component
public class MyEntityCollectionProcessor implements EntityCollectionProcessor {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private OData odata;
	private ServiceMetadata serviceMetadata;

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		
		// 1st we have retrieve the requested EntitySet from the uriInfo object (representation of the parsed service URI)
		  List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		  UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0); // in our example, the first segment is the EntitySet
		  EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		  
		  
		// 2nd: fetch the data from backend for this requested EntitySetName
		  // it has to be delivered as EntitySet object
		  EntityCollection entitySet = getData(edmEntitySet);
		  
		// 3rd: create a serializer based on the requested format (json)
		  ODataSerializer serializer = odata.createSerializer(responseFormat);
		  
		  
		  // 4th: Now serialize the content: transform from the EntitySet object to InputStream
		  EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		  ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
		  
		  final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
		  EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).contextURL(contextUrl).build();
		  SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet, opts);
		  InputStream serializedContent = serializerResult.getContent();

		  // Finally: configure the response object: set the body, headers and status code
		  response.setContent(serializedContent);
		  response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		  response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
		  
	}

	
	private EntityCollection getData(EdmEntitySet edmEntitySet){

		   EntityCollection collection = new EntityCollection();
		   
		   // check for which EdmEntitySet the data is requested
		   if(MyEdmProvider.ES_CUSTOMERS_NAME.equals(edmEntitySet.getName())) {
		       List<Entity> customerList = collection.getEntities();
		       
		       
		       List<Entity> result = jdbcTemplate.query("select id, name , email from customer", new CustomerMapper());
		       customerList.addAll(result);	      
		   }

		   return collection;
	}
	
	
	public static final class CustomerMapper implements RowMapper<Entity> {
		
		@Override
		public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
			Entity entity = new Entity()
	    			   .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, rs.getInt(1)))
	    			   .addProperty(new Property(null, "NAME", ValueType.PRIMITIVE, rs.getString(2)))
	    			   .addProperty(new Property(null, "EMAIL", ValueType.PRIMITIVE, rs.getString(3)));
			return entity;
		}
	}

}
