package com.partha.olingoapp03.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;


@Component
public class MyUtil {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Entity readCustomerData(EdmEntityType entityType, List<UriParameter> keyParams) throws ODataApplicationException{

		//here am assuming that am getting only id . else in real-scenario we have to read all keyparams 
		//and then build the sql query.
		Entity requestedEntity= null;
		try {
			requestedEntity = this.jdbcTemplate.queryForObject("select id , name , email from customer where id=?", 
													new Object[] {Integer.parseInt(keyParams.get(0).getText())} ,
													new RowMapper<Entity>() {

				@Override
				public Entity mapRow(ResultSet rs, int rowNum) throws SQLException {
					// TODO Auto-generated method stub
					Entity entity = new Entity()
							.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, rs.getInt(1)))
							.addProperty(new Property(null, "NAME", ValueType.PRIMITIVE, rs.getString(2)))
							.addProperty(new Property(null, "EMAIL", ValueType.PRIMITIVE, rs.getString(3)));
					return entity;	
				}
			});
			
			return requestedEntity;
			
		} catch (DataAccessException | NumberFormatException e) {
			e.printStackTrace();
		}

		if(requestedEntity == null){
			// this variable is null if our data doesn't contain an entity for the requested key
			// Throw suitable exception
			throw new ODataApplicationException("Entity for requested key doesn't exist",
					HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
		}

		return requestedEntity;
	}
	
	
	 public EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo) throws ODataApplicationException {

		    List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		    // To get the entity set we have to interpret all URI segments
		    if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
		      // Here we should interpret the whole URI but in this example we do not support navigation so we throw an
		      // exception
		      throw new ODataApplicationException("Invalid resource type for first segment.", HttpStatusCode.NOT_IMPLEMENTED
		          .getStatusCode(), Locale.ENGLISH);
		    }

		    UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths.get(0);

		    return uriResource.getEntitySet();
		  }


	public Entity createCustomer(EdmEntityType edmEntityType, Entity entityToCreate) {
		
			//for simplicity i have not kept any check on it
			String sql="insert  into customer (id,name,email) values (?,?,?)";
			Object[] args = new Object[] {entityToCreate.getProperty("ID").getValue(),
							(String)entityToCreate.getProperty("NAME").getValue(),
							(String)entityToCreate.getProperty("EMAIL").getValue()
					};
						
			this.jdbcTemplate.update(sql, args);
		    return entityToCreate;
			
//		// TODO Auto-generated method stub
//		// the ID of the newly created product entity is generated automatically
//	    int newId = 1;
//	    while (productIdExists(newId)) {
//	      newId++;
//	    }
//
//	    Property idProperty = entity.getProperty("ID");
//	    if (idProperty != null) {
//	      idProperty.setValue(ValueType.PRIMITIVE, Integer.valueOf(newId));
//	    } else {
//	      // as of OData v4 spec, the key property can be omitted from the POST request body
//	      entity.getProperties().add(new Property(null, "ID", ValueType.PRIMITIVE, newId));
//	    }
//	    entity.setId(createId("Products", newId));
//	    this.productList.add(entity);
//
//	    return entity;
	}

}
