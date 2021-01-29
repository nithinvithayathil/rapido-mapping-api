package com.ustglobal.rapido.mapping.application;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromDBMetaRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseRequestFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Application service to import columns from database tables as a datasource
 */
@Service
public class ImportDataSourceDefinitionFromDatabaseService {

  @Autowired
  private DataSourceFactoryProvider dataSourceFactoryProvider;

  @Autowired
  private ParseRequestFactory parseRequestFactory;

  /**
   * Imports all columns in specified list of tables in to a single datasource.
   * The method gets database configuration and table column details from external sources.
   * @param dataSourceName A name for the data source which will be created
   * @param dbConfigId DB configuration id of the database where the following schema and tables reside
   * @param schema Schema name of the database
   * @param tables Tables to be added.
   * @return The newly created DataSourceDefinition
   */
  public DataSourceDefinition importFromTableFields(String dataSourceName, String dbConfigId, String schema, List<String> tables){
    DataSourceFactory dataSourceFactory = getDBDataSourceFactory();

    ParseFromDBMetaRequest fromDBMetaRequest = createParseFromDBMetaRequest(dataSourceName, dbConfigId,
        schema, tables);

    DataSourceDefinitionParser parser = getDataSourceDefinitionParser(dataSourceFactory);

    return parser.parse(fromDBMetaRequest);

  }

  private DataSourceDefinitionParser getDataSourceDefinitionParser(
      DataSourceFactory dataSourceFactory) {
    return dataSourceFactory.createDataSourceDefinitionParser(ImportSourceType.DBMETA);
  }

  private DataSourceFactory getDBDataSourceFactory() {
    return dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.DATABASE);
  }

  private ParseFromDBMetaRequest createParseFromDBMetaRequest(String dataSourceName, String dbConfigId, String schema,
      List<String> tables) {
    ParseFromDBMetaRequest fromDBMetaRequest = createParseFromDBMetaRequest(dataSourceName);
    fromDBMetaRequest.setSchema(schema);
    fromDBMetaRequest.setTables(tables);
    fromDBMetaRequest.setDbConfigId(dbConfigId);
    return fromDBMetaRequest;
  }

  private ParseFromDBMetaRequest createParseFromDBMetaRequest(String dataSourceName) {
    return (ParseFromDBMetaRequest) parseRequestFactory.createParseRequest(
          ImportSourceType.DBMETA, dataSourceName);
  }

}
