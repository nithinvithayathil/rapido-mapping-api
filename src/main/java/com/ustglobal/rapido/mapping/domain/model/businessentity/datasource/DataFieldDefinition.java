package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import com.ustglobal.rapido.mapping.domain.shared.Persistable;

import com.ustglobal.rapido.mapping.domain.shared.Entity;
import org.springframework.data.annotation.Transient;

/**
 * Represents a single field in a DataSource
 */
public class DataFieldDefinition implements Entity<DataFieldDefinition>, Persistable {

	private FieldDefinitionId fieldDefinitionId;
	private String name;
	protected String xpath;
	@Transient
	private boolean hasChanges = false;

	public DataFieldDefinition(FieldDefinitionId fieldDefinitionId, String name) {
		DomainValidator.notNull(fieldDefinitionId, DomainErrorCode.PARAMETER_REQUIRED, "FieldDefinitionId");
		DomainValidator.notEmpty(name,DomainErrorCode.PARAMETER_REQUIRED,"Data Field Name");
		this.name = name;
		this.fieldDefinitionId = fieldDefinitionId;
		setXpath(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DataFieldDefinition that = (DataFieldDefinition) o;
		return sameIdentityAs(that);
	}

	@Override
	public boolean sameIdentityAs(DataFieldDefinition other) {
		return other != null && this.fieldDefinitionId.equals(other.fieldDefinitionId);
	}

	public FieldDefinitionId getFieldDefinitionId() {
		return fieldDefinitionId;
	}

	public String getName() {
		return name;
	}

	/**
	 * Default implementation for XPath of the field
	 * 
	 * @return
	 */
	public String getXpath() {
		return xpath;
	}

	private void setXpath(String xpath) {
		this.xpath = String.format("/%s", xpath);
	}

	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges;
	}

}
