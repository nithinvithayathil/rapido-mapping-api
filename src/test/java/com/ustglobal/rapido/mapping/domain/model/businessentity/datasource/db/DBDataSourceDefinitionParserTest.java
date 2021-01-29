package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.ustglobal.rapido.mapping.domain.DomainServiceRegistry;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinitionType;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceFactoryProvider;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromDBMetaRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromFileRequest;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;

@RunWith(MockitoJUnitRunner.class)
public class DBDataSourceDefinitionParserTest {

	@InjectMocks
	private DBDataSourceDefinitionParser dbDataSourceDefinitionParser;

	@Mock
	private DomainServiceRegistry domainServiceRegistry;

	@Mock
	private DatabaseQueryService databaseQueryService;

	@Mock
	private DataSourceFactoryProvider dataSourceFactoryProvider;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void parse_throws_exception_when_parserequest_is_not_an_instanceof_ParseFromDBMetaRequest() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Error while importing data source. Mostly related with file operations.");
		dbDataSourceDefinitionParser.parse(new ParseFromFileRequest(UUID.randomUUID().toString()));
	}

	@Test
	public void parse_throws_exception_when_parserequest_have_no_tableList() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Tables cannot be null or empty.");
		dbDataSourceDefinitionParser.parse(new ParseFromDBMetaRequest(UUID.randomUUID().toString()));
	}

	@Test
	public void parse() {
		String mockSchema = "MockSchema";
		String mockTable = "MockTable";
		String dbConfigId = UUID.randomUUID().toString();
		when(domainServiceRegistry.getDatabaseQueryService()).thenReturn(databaseQueryService);
		when(databaseQueryService.getColumnsForTable(mockSchema, mockTable, dbConfigId))
				.thenReturn(new ArrayList<String>(Arrays.asList("MockColumn")));
		when(dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.DATABASE))
				.thenReturn(new DBDataSourceFactory());

		ParseFromDBMetaRequest parseFromDBMetaRequest = new ParseFromDBMetaRequest(UUID.randomUUID().toString());
		parseFromDBMetaRequest.setDbConfigId(dbConfigId);
		parseFromDBMetaRequest.setTables(new ArrayList<String>(Arrays.asList(mockTable)));
		parseFromDBMetaRequest.setSchema(mockSchema);

		assertNotNull(dbDataSourceDefinitionParser.parse(parseFromDBMetaRequest));

		verify(domainServiceRegistry).getDatabaseQueryService();
		verify(databaseQueryService).getColumnsForTable(mockSchema, mockTable, dbConfigId);
		verify(dataSourceFactoryProvider, Mockito.times(2)).getDataSourceFactory(DataSourceDefinitionType.DATABASE);
	}

}
