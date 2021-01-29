package com.ustglobal.rapido.mapping.infrastructure.web;

public class ImportDataSourceFromFilesCommand {
  private String dataSourceDefinitionType;
  private String mainFileName;
  private String dataSourceName;

  public String getMainFileName() {
    return mainFileName;
  }

  public String getDataSourceDefinitionType() {
    return dataSourceDefinitionType;
  }

  public String getDataSourceName() {
    return dataSourceName;
  }
}
