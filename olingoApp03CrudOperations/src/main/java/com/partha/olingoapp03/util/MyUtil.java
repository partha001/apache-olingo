package com.partha.olingoapp03.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
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

}
