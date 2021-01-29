package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;

/**
 * Represents a single field in a DataSource if the source is DB
 */
public class DBDataFieldDefinition extends DataFieldDefinition {

	private String aliasName;

	public DBDataFieldDefinition(FieldDefinitionId fieldDefinitionId, String name) {
		super(fieldDefinitionId, name);
		setXpath(name);
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
		//Set xpath with aliasName if an alias name is set.
		setXpath(aliasName);
	}

	/**
	 * This method overrides the setXpath of base class
	 * 
	 * @param xpath
	 */
	private void setXpath(String xpath) {
		super.xpath = String.format("/%s", xpath);
	}
}
