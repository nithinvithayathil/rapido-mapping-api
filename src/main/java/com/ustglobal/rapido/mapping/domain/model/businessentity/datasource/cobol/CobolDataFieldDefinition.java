package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.cobol;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;

/**
 * Represents single data field in a DataSource if the source is Cobol data file
 */
public class CobolDataFieldDefinition extends DataFieldDefinition {

	public CobolDataFieldDefinition(FieldDefinitionId fieldDefinitionId, String name) {
		super(fieldDefinitionId, name);
	}

}
