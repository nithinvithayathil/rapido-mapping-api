package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import com.ustglobal.rapido.mapping.domain.shared.Persistable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


import com.ustglobal.rapido.mapping.domain.model.businessentity.transformation.Transformation;
import com.ustglobal.rapido.mapping.domain.shared.Entity;
import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;

/**
 * Represents the mapping of source field and target field that need to be used
 * in a BusinessEntity.
 * 
 *
 */
public class DataFieldMapping implements Entity<DataFieldMapping>, Persistable {

	private String fieldMappingId;
	private FieldDefinitionId targetFieldId;
	private Collection<FieldDefinitionId> sourceFieldIds;
	private Transformation combinedTransformation;
	private Collection<FieldTransformation> sourceTransformations;
	@Transient
	private boolean hasChanges = false;
	private boolean isDefault = false;

	public DataFieldMapping(){}

	public DataFieldMapping(String fieldMappingId, FieldDefinitionId targetFieldId,
			Collection<FieldDefinitionId> sourceFieldIds, Transformation combinedTransformation,
			Collection<FieldTransformation> sourceTransformations, boolean isDefault) {
		validateMappingForSourceFieldOrDefaultValue(sourceFieldIds, isDefault);
		DomainValidator.notNull(targetFieldId, DomainErrorCode.PARAMETER_REQUIRED, "TargetFieldId");
		DomainValidator.notEmpty(fieldMappingId, DomainErrorCode.PARAMETER_REQUIRED, "FieldMappingId");
		this.fieldMappingId = fieldMappingId;
		this.targetFieldId = targetFieldId;
		this.sourceFieldIds = sourceFieldIds;
		this.combinedTransformation = combinedTransformation;
		this.sourceTransformations = sourceTransformations;
		this.isDefault= isDefault;
	}

	private void validateMappingForSourceFieldOrDefaultValue(Collection<FieldDefinitionId> sourceFieldIds,
			boolean isDefault) {
		if (isDefault && !CollectionUtils.isEmpty(sourceFieldIds)) {
			throw new DomainException(DomainErrorCode.SOURCE_NOT_REQUIRED_FOR_DEFAULT_MAPPING, null);
		} else if (CollectionUtils.isEmpty(sourceFieldIds) && !isDefault) {
			throw new DomainException(DomainErrorCode.SOURCE_REQUIRED_FOR_NONDEFAULT_MAPPING, null);
		}
	}

	public DataFieldMapping(String fieldMappingId, FieldDefinitionId targetFieldId,
			Collection<FieldDefinitionId> sourceFieldIds) {
		this(fieldMappingId, targetFieldId, sourceFieldIds, null, null, false);
	}

	public DataFieldMapping(String fieldMappingId, FieldDefinitionId targetFieldId,
			Collection<FieldDefinitionId> sourceFieldIds, boolean isDefault) {
		this(fieldMappingId, targetFieldId, sourceFieldIds, null, null, isDefault);
	}

	public void addSourceFields(FieldDefinitionId fieldId) {
		DomainValidator.notNull(fieldId, DomainErrorCode.PARAMETER_REQUIRED, "FieldDefinitionId");
		if (this.sourceFieldIds == null) {
			sourceFieldIds = new ArrayList<>();
		}
		sourceFieldIds.add(fieldId);
		hasChanges = true;
	}

	public void removeSourceField(FieldDefinitionId fieldId) {
		DomainValidator.notNull(fieldId, DomainErrorCode.PARAMETER_REQUIRED, "FieldDefinitionId");
		if (this.sourceFieldIds != null) {
			sourceFieldIds.remove(fieldId);
			hasChanges = true;
		}
	}
	
	public boolean isSourceMapped(String sourceDefinitionId) {
		DomainValidator.notNull(sourceDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "SourceDefinitionId");
		return this.getSourceFieldIds().stream()
				.anyMatch(field -> field.getSourceDefinitionId().equals(sourceDefinitionId));
	}

	public void addSourceTransformations(FieldTransformation transformation) {
		DomainValidator.notNull(transformation, DomainErrorCode.PARAMETER_REQUIRED, "FieldTransformation");
		validateIfSourceFieldExist(transformation.getFieldDefinitionId());
		if (this.sourceTransformations == null) {
			sourceTransformations = new ArrayList<>();
		}
		sourceTransformations.add(transformation);
		hasChanges = true;
	}

  public void removeSourceTransformationByFieldId(FieldDefinitionId fieldDefinitionId){
    DomainValidator.notNull(fieldDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "FieldDefinitionId");
    if(this.sourceTransformations != null){
      this.sourceTransformations.removeIf(fieldTransformation -> fieldTransformation.getFieldDefinitionId().equals(fieldDefinitionId));
      hasChanges = true;
    }
  }

  public void assignCombineTransform(Transformation transformation) {
    DomainValidator.notNull(transformation, DomainErrorCode.PARAMETER_REQUIRED, "Transformation");
    this.combinedTransformation = transformation;
    hasChanges = true;
  }

  public void removeCombineTransform() {
    this.combinedTransformation = null;
    hasChanges = true;
  }

	@Override
	public boolean sameIdentityAs(DataFieldMapping other) {
		return other != null && other.fieldMappingId.equals(this.fieldMappingId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DataFieldMapping that = (DataFieldMapping) o;
		return sameIdentityAs(that);
	}

	public String getFieldMappingId() {
		return fieldMappingId;
	}

	public FieldDefinitionId getTargetFieldId() {
		return targetFieldId;
	}

	public Collection<FieldDefinitionId> getSourceFieldIds() {
		if(sourceFieldIds == null){
			return null;
		}
		return Collections.unmodifiableCollection(sourceFieldIds);
	}

	public Transformation getCombinedTransformation() {
		return combinedTransformation;
	}

	public Collection<FieldTransformation> getSourceTransformations() {
		if(sourceTransformations == null){
			return null;
		}
		return Collections.unmodifiableCollection(sourceTransformations);
	}

	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges || (combinedTransformation != null && combinedTransformation.hasNonPersistedChanges())
			|| (sourceTransformations != null && sourceTransformations.stream().anyMatch(FieldTransformation::hasNonPersistedChanges));
	}

	private void validateIfSourceFieldExist(FieldDefinitionId fieldDefinitionId) {
		if (!this.getSourceFieldIds().contains(fieldDefinitionId)) {
			throw new DomainException(DomainErrorCode.SOURCE_FIELD_NOT_EXISTING_IN_MAPPING, null, fieldDefinitionId,
					this.fieldMappingId);
		}
	}

	public boolean isDefault() {
		return this.isDefault;
	}

}
