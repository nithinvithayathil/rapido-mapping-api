package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest;

/**
 * A request for parsing data source
 */
public abstract class ParseRequest {

	public static final ParseRequest NONE = new NoParseRequest();

	private String dataSourceName;

	public ParseRequest() {
	}

	public ParseRequest(String dataSourceName){
    this.dataSourceName = dataSourceName;
  }

  public String getDataSourceName() {
    return dataSourceName;
  }
}
