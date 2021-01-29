package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import static org.junit.Assert.*;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
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
public class DBDataSourceFactoryTest {

	@Mock
	private DBDataSourceDefinitionParser dbDataSourceDefinitionParser;
	
	@InjectMocks
	private DBDataSourceFactory dbDataSourceFactory;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void createDataSourceDefinition_returns_new_datasourcedefinition() {
		DataSourceDefinition dataSourceDefinition = dbDataSourceFactory.createDataSourceDefinition(new CreateDataSourceDefinitionRequest("1", "name"));
		assertTrue(dataSourceDefinition instanceof DBDataSourceDefinition);
	}

	@Test
	public void createDataFieldDefinition_returns_new_datafielddefinition() {
    DataFieldDefinition dataFieldDefinition = dbDataSourceFactory.createDataFieldDefinition(new CreateDataFieldDefinitionRequest(new FieldDefinitionId("1","1"), "name"));
    assertTrue(dataFieldDefinition instanceof DBDataFieldDefinition);
	}

	@Test
	public void createDataSourceDefinitionParser_returns_dbfiledatasourceparser_when_importtype_is_DBMeta() {
		assertTrue("Should return dbdatasourceparser.", dbDataSourceFactory
				.createDataSourceDefinitionParser(ImportSourceType.DBMETA) instanceof DBDataSourceDefinitionParser);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_file() {
		expectedException.expect(NotImplementedException.class);
		expectedException.expectMessage("File based import is not supported for DB datasource now.");
		dbDataSourceFactory.createDataSourceDefinitionParser(ImportSourceType.FILE);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_Null() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("ImportSourceType cannot be null or empty.");
		dbDataSourceFactory.createDataSourceDefinitionParser(null);
	}
}