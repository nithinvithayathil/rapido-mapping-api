package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinitionParser;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromFileRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseRequest;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.springframework.stereotype.Service;

/**
 * Parses DataSourceDefinition from a Cobol file
 */
@Service
public class CobolFileDataSourceDefinitionParser implements DataSourceDefinitionParser {
    @Override
    public DataSourceDefinition parse(ParseRequest request) {
        DomainValidator.isInstanceOf(ParseFromFileRequest.class, request, DomainErrorCode.DATASOURCE_IMPORT_ERROR);

        ParseFromFileRequest fromFileRequest = (ParseFromFileRequest) request;

        return null;
    }
}
