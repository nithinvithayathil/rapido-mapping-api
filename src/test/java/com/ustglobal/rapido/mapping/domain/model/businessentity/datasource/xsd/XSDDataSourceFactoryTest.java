package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

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
public class XSDDataSourceFactoryTest {

	@Mock
	private XSDFileDataSourceDefinitionParser xsdFileDataSourceDefinitionParser;

	@InjectMocks
	private XSDDataSourceFactory xsdDataSourceFactory;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void createDataSourceDefinition_returns_new_datasourcedefinition() {
		DataSourceDefinition dataSourceDefinition = xsdDataSourceFactory.createDataSourceDefinition(new CreateDataSourceDefinitionRequest("1", "name"));
		assertTrue(dataSourceDefinition instanceof XSDDataSourceDefinition);
	}

	@Test
	public void createDataFieldDefinition_returns_new_datafielddefinition() {
		DataFieldDefinition dataFieldDefinition = xsdDataSourceFactory.createDataFieldDefinition(new CreateDataFieldDefinitionRequest(new FieldDefinitionId("1","1"), "name"));
		assertTrue(dataFieldDefinition instanceof XSDDataFieldDefinition);
	}

	@Test
	public void createDataSourceDefinitionParser_returns_xsdfiledatasourceparser_when_importtype_is_File() {
		assertTrue("Should return xsdfiledatasourceparser.", xsdDataSourceFactory
				.createDataSourceDefinitionParser(ImportSourceType.FILE) instanceof XSDFileDataSourceDefinitionParser);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_DBMeta() {
		expectedException.expect(NotImplementedException.class);
		expectedException.expectMessage("DB based import is not supported for XSD now.");
		xsdDataSourceFactory.createDataSourceDefinitionParser(ImportSourceType.DBMETA);
	}

	@Test
	public void createDataSourceDefinitionParser_throws_exception_when_importtype_is_Null() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("ImportSourceType cannot be null or empty.");
		xsdDataSourceFactory.createDataSourceDefinitionParser(null);
	}
}