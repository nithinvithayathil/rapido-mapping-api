package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;

/**
 * Represents DB DataSources that need to be used in a BusinessEntity for source
 * and targets.
 * 
 */
public class DBDataSourceDefinition extends DataSourceDefinition {

	public DBDataSourceDefinition(String sourceDefinitionId, String name) {
		super(sourceDefinitionId, name);
	}

}
