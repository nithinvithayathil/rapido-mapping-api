package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import org.springframework.data.annotation.Transient;

import com.ustglobal.rapido.mapping.domain.model.businessentity.transformation.Transformation;
import com.ustglobal.rapido.mapping.domain.shared.Entity;
import com.ustglobal.rapido.mapping.domain.shared.Persistable;

public class FieldTransformation implements Entity<FieldTransformation>, Persistable {
	private FieldDefinitionId fieldDefinitionId;
	private Transformation transformation;
	@Transient
	private boolean hasChanges;

	public FieldTransformation(FieldDefinitionId fieldDefinitionId, Transformation transformation) {
		this.fieldDefinitionId = fieldDefinitionId;
		this.transformation = transformation;
	}

	public FieldTransformation(FieldDefinitionId fieldDefinitionId) {
		this(fieldDefinitionId, null);
	}

	public FieldTransformation() {

	}

	public FieldDefinitionId getFieldDefinitionId() {
		return fieldDefinitionId;
	}

	public Transformation getTransformation() {
		return transformation;
	}

	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		FieldTransformation that = (FieldTransformation) o;
		return sameIdentityAs(that);
	}

	@Override
	public boolean sameIdentityAs(FieldTransformation other) {
		return other != null && other.fieldDefinitionId.equals(this.fieldDefinitionId);
	}

}
