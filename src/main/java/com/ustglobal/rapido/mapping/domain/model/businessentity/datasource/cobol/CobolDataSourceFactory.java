package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory for all types related to Cobol datasource source
 *
 * Cobol data source is any source that is imported using a COBOL copy book.
 */
@Service
public class CobolDataSourceFactory implements DataSourceFactory {

    @Autowired
    private CobolFileDataSourceDefinitionParser cobolFileDataSourceDefinitionParser;

    @Override
    public DataSourceDefinition createDataSourceDefinition(CreateDataSourceDefinitionRequest sourceDefinitionRequest) {
        return new CobolDataSourceDefinition(sourceDefinitionRequest.getSourceDefinitionId(), sourceDefinitionRequest.getName());
    }

    @Override
    public DataFieldDefinition createDataFieldDefinition(CreateDataFieldDefinitionRequest fieldDefinitionRequest) {
        return new CobolDataFieldDefinition(fieldDefinitionRequest.getFieldDefinitionId(), fieldDefinitionRequest.getFieldName());
    }

    @Override
    public DataSourceDefinitionParser createDataSourceDefinitionParser(ImportSourceType importSourceType) {
        DomainValidator.notNull(importSourceType, DomainErrorCode.PARAMETER_REQUIRED, "ImportSourceType");
        switch (importSourceType){

            case DBMETA:
                throw new NotImplementedException("DB based import is not supported for Cobol now.");

            case FILE:
                return cobolFileDataSourceDefinitionParser;

        }
        return null;
    }

}
