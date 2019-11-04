package com.partha.olingoapp03.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.olingo.commons.api.ex.ODataException;
import org.springframework.stereotype.Component;

@Component
public class MyEdmProvider extends CsdlAbstractEdmProvider {
	
	// Service Namespace
	public static final String NAMESPACE = "OData.Demo";

	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// Entity Types Names
	public static final String ET_CUSTOMER_NAME = "Customer";
	public static final FullQualifiedName ET_CUSTOMER_FQN = new FullQualifiedName(NAMESPACE, ET_CUSTOMER_NAME);

	// Entity Set Names
	public static final String ES_CUSTOMERS_NAME = "Customers";
	
	

	@Override
	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
		
		 CsdlEntityType entityType = null;
		 
		if (entityTypeName.equals(ET_CUSTOMER_FQN)) {
		      // create EntityType properties
		      CsdlProperty id = new CsdlProperty().setName("ID")
		          .setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		      CsdlProperty name = new CsdlProperty().setName("NAME")
		          .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		      CsdlProperty email = new CsdlProperty().setName("EMAIL")
		          .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		      
		      // create PropertyRef for Key element
		      CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		      propertyRef.setName("ID");


		      // configure EntityType
		      entityType = new CsdlEntityType();
		      entityType.setName(ET_CUSTOMER_NAME);
		      entityType.setProperties(Arrays.asList(id, name, email));
//		      entityType.setKey(Arrays.asList(propertyRef));
//		      entityType.setNavigationProperties(navPropList);
		      entityType.setKey(Arrays.asList(propertyRef));
		}
		
		return entityType;
		
	}

	@Override
	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
		if(entityContainer.equals(CONTAINER)){
		    if(entitySetName.equals(ES_CUSTOMERS_NAME)){
		      CsdlEntitySet entitySet = new CsdlEntitySet();
		      entitySet.setName(ES_CUSTOMERS_NAME);
		      entitySet.setType(ET_CUSTOMER_FQN);
		      return entitySet;
		    }
		  }

		  return null;
	}
	
	
	public CsdlEntityContainer getEntityContainer() throws ODataException {

		  // create EntitySets
		  List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		  entitySets.add(getEntitySet(CONTAINER, ES_CUSTOMERS_NAME));

		  // create EntityContainer
		  CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		  entityContainer.setName(CONTAINER_NAME);
		  entityContainer.setEntitySets(entitySets);

		  return entityContainer;
		}
	
	
	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

	    // This method is invoked when displaying the Service Document at e.g. http://localhost:8080/DemoService/DemoService.svc
	    if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
	        CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
	        entityContainerInfo.setContainerName(CONTAINER);
	        return entityContainerInfo;
	    }

	    return null;
	}
	
	
	public List<CsdlSchema> getSchemas() throws ODataException {

		  // create Schema
		  CsdlSchema schema = new CsdlSchema();
		  schema.setNamespace(NAMESPACE);

		  // add EntityTypes
		  List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		  entityTypes.add(getEntityType(ET_CUSTOMER_FQN));
		  schema.setEntityTypes(entityTypes);

		  // add EntityContainer
		  schema.setEntityContainer(getEntityContainer());

		  // finally
		  List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		  schemas.add(schema);

		  return schemas;
		}

	
	
}
