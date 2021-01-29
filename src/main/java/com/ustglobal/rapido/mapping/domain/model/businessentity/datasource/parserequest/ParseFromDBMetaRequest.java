package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest;

import java.util.List;

public class ParseFromDBMetaRequest extends ParseRequest {

	private String dbConfigId;
	private String schema;
	private List<String> tables;

	public ParseFromDBMetaRequest(String dataSourceName) {
		super(dataSourceName);
	}

	public String getDBConfigId() {
		return dbConfigId;
	}

	public void setDbConfigId(String dbConfigId) {
		this.dbConfigId = dbConfigId;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<String> getTables() {
		return tables;
	}

	public void setTables(List<String> tables) {
		this.tables = tables;
	}
}
