package com.ustglobal.rapido.mapping.infrastructure.web;

import java.util.List;

public class ImportDataSourceFromDbCommand {

  private String dbConfigId;
  private String schema;
  private List<String> tables;
  /**
   * A name for the new datasource definition created as part of the request.
   */
  private String dataSourceName;

  public String getDbConfigId() {
    return dbConfigId;
  }

  public String getSchema() {
    return schema;
  }

  public List<String> getTables() {
    return tables;
  }

  public String getDataSourceName() {
    return dataSourceName;
  }

  @Override
  public String toString() {
    return String.format("{dbconfig: %s, schema : %s, dataSourceName: %s, tables: %s",
        dbConfigId,schema, dataSourceName, tables);
  }
}
