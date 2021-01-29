package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol;

import static org.junit.Assert.*;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.CreateDataFieldDefinitionRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.CreateDataSourceDefinitionRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CobolDataSourceFactoryTest {

	@Mock
	private CobolFileDataSourceDefinitionParser cobolFileDataSourceDefinitionParser;
	
	@InjectMocks
	private CobolDataSourceFactory cobolDataSourceFactory ;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void createDataSourceDefinition_returns_new_datasourcedefinition() {
		DataSourceDefinition dataSourceDefinition = cobolDataSourceFactory.createDataSourceDefinition(new CreateDataSourceDefinitionRequest("1", "name"));
		assertTrue(dataSourceDefinition instanceof CobolDataSourceDefinition);
	}

	@Test
	public void createDataFieldDefinition_returns_new_datafielddefinition() {
		DataFieldDefinition dataFieldDefinition = cobolDataSourceFactory.createDataFieldDefinition(new CreateDataFieldDefinitionRequest(new FieldDefinitionId("1","1"), "name"));
		assertTrue(dataFieldDefinition instanceof CobolDataFieldDefinition);
	}

	@Test
	public void createDataSourceDefinitionParser_returns_cobolfiledatasourceparser_when_importtype_is_File() {
		assertTrue("Should return cobolfiledatasourceparser.", cobolDataSourceFactory.createDataSourceDefinitionParser(
				ImportSourceType.FILE) instanceof CobolFileDataSourceDefinitionParser);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_DBMeta() {
		expectedException.expect(NotImplementedException.class);
		expectedException.expectMessage("DB based import is not supported for Cobol now.");
		CobolDataSourceFactory cobolDataSourceFactory = new CobolDataSourceFactory();
		cobolDataSourceFactory.createDataSourceDefinitionParser(ImportSourceType.DBMETA);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_Null() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("ImportSourceType cannot be null or empty.");
		CobolDataSourceFactory cobolDataSourceFactory = new CobolDataSourceFactory();
		cobolDataSourceFactory.createDataSourceDefinitionParser(null);
	}
}