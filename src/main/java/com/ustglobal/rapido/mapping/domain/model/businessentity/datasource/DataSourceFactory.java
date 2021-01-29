package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;

/**
 * Interface to all datasource factory implementations.
 */
public interface DataSourceFactory {
    DataSourceDefinition createDataSourceDefinition(CreateDataSourceDefinitionRequest sourceDefinitionRequest);
    DataFieldDefinition createDataFieldDefinition(CreateDataFieldDefinitionRequest fieldDefinitionRequest);
    DataSourceDefinitionParser createDataSourceDefinitionParser(ImportSourceType importSourceType);
}
