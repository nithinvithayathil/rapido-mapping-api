package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory for all types related to Database Data source
 *
 * Database data source is any source which derives its field info from tables and columns in a database.
 *
 */
@Service
public class DBDataSourceFactory implements DataSourceFactory {

    @Autowired
    private DBDataSourceDefinitionParser dbDataSourceDefinitionParser;

    @Override
    public DataSourceDefinition createDataSourceDefinition(CreateDataSourceDefinitionRequest sourceDefinitionRequest) {
        return new DBDataSourceDefinition(sourceDefinitionRequest.getSourceDefinitionId(), sourceDefinitionRequest.getName());
    }

    @Override
    public DataFieldDefinition createDataFieldDefinition(CreateDataFieldDefinitionRequest fieldDefinitionRequest) {
        return new DBDataFieldDefinition(fieldDefinitionRequest.getFieldDefinitionId(), fieldDefinitionRequest.getFieldName());
    }

    @Override
    public DataSourceDefinitionParser createDataSourceDefinitionParser(ImportSourceType importSourceType) {
        DomainValidator.notNull(importSourceType, DomainErrorCode.PARAMETER_REQUIRED, "ImportSourceType");
        switch (importSourceType){

            case DBMETA:
                return dbDataSourceDefinitionParser;

            case FILE:
                throw new NotImplementedException("File based import is not supported for DB datasource now.");
        }
        return null;
    }

}
