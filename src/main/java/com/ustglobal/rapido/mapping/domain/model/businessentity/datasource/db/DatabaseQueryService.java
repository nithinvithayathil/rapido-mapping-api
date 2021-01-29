package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import java.util.Collection;

/**
 * Service to interact and query databases using Database Configuration
 */
public interface DatabaseQueryService {

	public Collection<String> getColumnsForTable(String schema, String tableName, String dbConfigId);

}
