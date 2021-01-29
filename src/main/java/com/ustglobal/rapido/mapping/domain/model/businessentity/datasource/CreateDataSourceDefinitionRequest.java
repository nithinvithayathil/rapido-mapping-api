package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

public class CreateDataSourceDefinitionRequest {

  private String sourceDefinitionId;
  private String name;

  public CreateDataSourceDefinitionRequest(String sourceDefinitionId, String name){

    this.sourceDefinitionId = sourceDefinitionId;
    this.name = name;
  }

  public String getSourceDefinitionId() {
    return sourceDefinitionId;
  }

  public String getName() {
    return name;
  }
}
