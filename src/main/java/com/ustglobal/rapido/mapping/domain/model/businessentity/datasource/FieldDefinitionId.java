package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;

import com.ustglobal.rapido.mapping.domain.shared.ValueObject;

/**
 * Represents unique identifier of a single field in a DataSource
 */
public class FieldDefinitionId implements ValueObject<FieldDefinitionId> {

	private static final long serialVersionUID = -696420802726027413L;

	private String id;
	private String sourceDefinitionId;

	public FieldDefinitionId(String id, String sourceDefinitionId) {
		DomainValidator.notEmpty(id, DomainErrorCode.PARAMETER_REQUIRED, "Id of field");
		DomainValidator.notEmpty(sourceDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "SourceDefinitionId");
		this.id = id;
		this.sourceDefinitionId = sourceDefinitionId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FieldDefinitionId that = (FieldDefinitionId) o;
		return sameValueAs(that);
	}

	@Override
	public boolean sameValueAs(FieldDefinitionId other) {
		return other != null && this.id.equals(other.id) && this.sourceDefinitionId.equals(other.sourceDefinitionId);
	}

	public String getId() {
		return id;
	}

	public String getSourceDefinitionId() {
		return sourceDefinitionId;
	}

	@Override
	public String toString() {
		return String.format("FieldId: %s, SourceId: %s", id, sourceDefinitionId);
	}
}
