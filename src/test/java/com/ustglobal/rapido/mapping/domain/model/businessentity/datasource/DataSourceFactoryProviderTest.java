package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import static org.junit.Assert.*;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol.CobolDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db.DBDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd.XSDDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceFactoryProviderTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private XSDDataSourceFactory xsdDataSourceFactory;

	@Mock
	private CobolDataSourceFactory cobolDataSourceFactory;

	@Mock
	private DBDataSourceFactory dbDataSourceFactory;

	@InjectMocks
	DataSourceFactoryProvider provider;

	@Test
	public void getDataSourceFactory_returns_XSDFactory_when_definition_type_is_xsd() {
		assertTrue(provider.getDataSourceFactory(DataSourceDefinitionType.XSD) instanceof XSDDataSourceFactory);
	}

	@Test
	public void getDataSourceFactory_returns_COBOLFactory_when_definition_type_is_cobol() {
		assertTrue(provider.getDataSourceFactory(DataSourceDefinitionType.COBOL) instanceof CobolDataSourceFactory);
	}

	@Test
	public void getDataSourceFactory_returns_DBFactory_when_definition_type_is_Database() {
		assertTrue(provider.getDataSourceFactory(DataSourceDefinitionType.DATABASE) instanceof DBDataSourceFactory);
	}

	@Test
	public void getDataSourceFactory_throws_exception_when_definition_type_is_Null() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("DataSourceDefinitionType cannot be null or empty.");
		DataSourceFactoryProvider provider = new DataSourceFactoryProvider();
		provider.getDataSourceFactory(null);
	}
}