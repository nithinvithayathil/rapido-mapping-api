package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ImportSourceType;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory for all types related to XSD Data source
 *
 * XSD Data source is any XML based data source whose schema is imported from XSD files.
 */
@Service
public class XSDDataSourceFactory implements DataSourceFactory {

    @Autowired
    private XSDFileDataSourceDefinitionParser xsdFileDataSourceDefinitionParser;

    @Override
    public DataSourceDefinition createDataSourceDefinition(CreateDataSourceDefinitionRequest sourceDefinitionRequest) {
        return new XSDDataSourceDefinition(sourceDefinitionRequest.getSourceDefinitionId(), sourceDefinitionRequest.getName());
    }

	@Override
	public DataFieldDefinition createDataFieldDefinition(CreateDataFieldDefinitionRequest fieldDefinitionRequest) {
		return new XSDDataFieldDefinition(fieldDefinitionRequest.getFieldDefinitionId(),
				fieldDefinitionRequest.getFieldName(), fieldDefinitionRequest.getXpath());
	}

    @Override
    public DataSourceDefinitionParser createDataSourceDefinitionParser(ImportSourceType importSourceType) {
      DomainValidator
          .notNull(importSourceType, DomainErrorCode.PARAMETER_REQUIRED, "ImportSourceType");
        switch (importSourceType){

            case DBMETA:
                throw new NotImplementedException("DB based import is not supported for XSD now.");

            case FILE:
                return xsdFileDataSourceDefinitionParser;

        }
        return null;
    }
}
