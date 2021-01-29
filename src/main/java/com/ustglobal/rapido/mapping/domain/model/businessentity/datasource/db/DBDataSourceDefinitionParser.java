package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.*;
import com.ustglobal.rapido.mapping.domain.DomainServiceRegistry;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import java.util.Collection;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Parses DataSourceDefinition from DBMeta data
 */
@Service
public class DBDataSourceDefinitionParser implements DataSourceDefinitionParser {
	@Autowired
	private DomainServiceRegistry domainServiceRegistry;

	@Autowired
	private DataSourceFactoryProvider dataSourceFactoryProvider;

	@Override
	public DataSourceDefinition parse(ParseRequest request) {
		DomainValidator.isInstanceOf(ParseFromDBMetaRequest.class, request, DomainErrorCode.DATASOURCE_IMPORT_ERROR);

		ParseFromDBMetaRequest fromDBMetaRequest = (ParseFromDBMetaRequest) request;
		DomainValidator.notEmpty(fromDBMetaRequest.getTables(), DomainErrorCode.PARAMETER_REQUIRED, "Tables");

		DataSourceDefinition newDBDatasourceDefinition = createNewDataSourceDefinition(request, fromDBMetaRequest);

		for (String table : fromDBMetaRequest.getTables()) {
			Collection<String> columns = getColumnNamesForTable(fromDBMetaRequest.getSchema(), fromDBMetaRequest.getDBConfigId(), table);

			if (CollectionUtils.isEmpty(columns)) {
				continue;
			}
			addFieldDefinitionsToDataSourceFromColumns(newDBDatasourceDefinition, request, table, columns);
		}

		return newDBDatasourceDefinition;
	}

	private void addFieldDefinitionsToDataSourceFromColumns(DataSourceDefinition newDBDatasourceDefinition,
			ParseRequest request, String table, Collection<String> columns) {
		for (String column : columns) {
			DBDataFieldDefinition dbDataFieldDefinition = createDbDataFieldDefinition(request,
					newDBDatasourceDefinition, column);
			dbDataFieldDefinition.setAliasName(String.format("%s.%s", table, column));
			newDBDatasourceDefinition.addField(dbDataFieldDefinition);
		}
	}

	private DBDataFieldDefinition createDbDataFieldDefinition(ParseRequest request,
			DataSourceDefinition datasourceDefinition, String column) {
		return (DBDataFieldDefinition) dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.DATABASE)
				.createDataFieldDefinition(getCreateFieldDefinitionRequest(datasourceDefinition, column));
	}

	private CreateDataFieldDefinitionRequest getCreateFieldDefinitionRequest(DataSourceDefinition datasourceDefinition,
			String column) {
		return new CreateDataFieldDefinitionRequest(
				new FieldDefinitionId(UUID.randomUUID().toString(), datasourceDefinition.getSourceDefinitionId()),
				column);
	}

	private Collection<String> getColumnNamesForTable(String schema, String dbConfigId, String table) {
		return domainServiceRegistry.getDatabaseQueryService().getColumnsForTable(schema, table, dbConfigId);
	}

	private DataSourceDefinition createNewDataSourceDefinition(ParseRequest request,
			ParseFromDBMetaRequest fromDBMetaRequest) {
		return dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.DATABASE)
				.createDataSourceDefinition(new CreateDataSourceDefinitionRequest(UUID.randomUUID().toString(),
						StringUtils.isEmpty(fromDBMetaRequest.getDataSourceName()) ? "DB Datasource"
								: fromDBMetaRequest.getDataSourceName()));
	}
}
