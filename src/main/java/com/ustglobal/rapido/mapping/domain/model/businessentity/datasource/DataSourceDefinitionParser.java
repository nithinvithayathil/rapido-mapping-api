package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseRequest;

public interface DataSourceDefinitionParser {

	DataSourceDefinition parse(ParseRequest request);
	
}
