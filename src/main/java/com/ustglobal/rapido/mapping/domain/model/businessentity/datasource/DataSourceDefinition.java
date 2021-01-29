package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import com.ustglobal.rapido.mapping.domain.shared.Entity;
import com.ustglobal.rapido.mapping.domain.shared.Persistable;

import java.util.Collections;
import java.util.Optional;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.annotation.Transient;

/**
 * Represents the DataSources that need to be used in a BusinessEntity for
 * source and targets. DataSource will have a type which describes what kind of
 * data source is it.
 */
public class DataSourceDefinition implements Entity<DataSourceDefinition>, Persistable {

	private String sourceDefinitionId;
	private String name;
	private Collection<DataFieldDefinition> fields;
	@Transient
	private boolean hasChanges = false;

	public DataSourceDefinition(String sourceDefinitionId, String name, Collection<DataFieldDefinition> fields) {

		DomainValidator.notEmpty(sourceDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "SourceDefinitionId");
		DomainValidator.notEmpty(name, DomainErrorCode.PARAMETER_REQUIRED, "Data Source Name");
		this.sourceDefinitionId = sourceDefinitionId;
		this.name = name;
		this.fields = fields;
	}

	public DataSourceDefinition(String sourceDefinitionId, String name) {
		this(sourceDefinitionId, name, null);
	}

	public DataSourceDefinition() {

	}

	public void addField(DataFieldDefinition field) {
		DomainValidator.notNull(field, DomainErrorCode.PARAMETER_REQUIRED, "DataFieldDefinition");
		if (this.fields == null) {
			fields = new ArrayList<>();
		}
		fields.add(field);
		hasChanges = true;
	}

	public void removeField(DataFieldDefinition field) {
		DomainValidator.notNull(field, DomainErrorCode.PARAMETER_REQUIRED, "DataFieldDefinition");
		if (this.fields != null) {
			fields.remove(field);
			hasChanges = true;
		}
	}

	public void removeFieldById(FieldDefinitionId id) {
		DataFieldDefinition existingDataFieldDefinition = findDataFieldDefinitionById(id);
		if (existingDataFieldDefinition != null) {
			removeField(existingDataFieldDefinition);
		}
	}

	public DataFieldDefinition findDataFieldDefinitionById(FieldDefinitionId fieldDefinitionId) {
		DomainValidator.notNull(fieldDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "FieldDefinitionId");
		Optional<DataFieldDefinition> dataFieldDefinition = this.fields.stream()
				.filter(fieldDefinition -> fieldDefinition.getFieldDefinitionId().equals(fieldDefinitionId))
				.findFirst();
		return dataFieldDefinition.isPresent() ? dataFieldDefinition.get() : null;
	}

	@Override
	public boolean sameIdentityAs(DataSourceDefinition other) {
		return other != null && other.sourceDefinitionId.equals(this.sourceDefinitionId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DataSourceDefinition that = (DataSourceDefinition) o;
		return sameIdentityAs(that);
	}

	public String getSourceDefinitionId() {
		return sourceDefinitionId;
	}

	public String getName() {
		return name;
	}

	public Collection<DataFieldDefinition> getFields() {
		if(fields == null){
			return null;
		}
		return Collections.unmodifiableCollection(fields);
	}

	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges || (fields != null && fields.stream().anyMatch(DataFieldDefinition::hasNonPersistedChanges));
	}
}
