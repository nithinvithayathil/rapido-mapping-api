package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol.CobolDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db.DBDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd.XSDDataSourceFactory;
import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides factories for creating different kind datasource objects.
 */
@Service
public class DataSourceFactoryProvider {

	@Autowired
	private XSDDataSourceFactory xsdDataSourceFactory;
	
	@Autowired
	private CobolDataSourceFactory cobolDataSourceFactory;
	
	@Autowired
	private DBDataSourceFactory dbDataSourceFactory;

	public DataSourceFactory getDataSourceFactory(DataSourceDefinitionType definitionType) {

		DomainValidator.notNull(definitionType, DomainErrorCode.PARAMETER_REQUIRED, "DataSourceDefinitionType");

		switch (definitionType) {
		case XSD:
			return xsdDataSourceFactory;
		case COBOL:
			return cobolDataSourceFactory;
		case DATABASE:
			return dbDataSourceFactory;
		}
		return null;
	}

}
