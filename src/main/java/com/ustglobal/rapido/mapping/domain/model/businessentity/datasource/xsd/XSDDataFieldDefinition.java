package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;

/**
 * Represents single data field in a DataSource if the source is XSD
 */
public class XSDDataFieldDefinition extends DataFieldDefinition {

	public XSDDataFieldDefinition(FieldDefinitionId fieldDefinitionId, String name, String xpath) {
		super(fieldDefinitionId, name);
		setXpath(xpath);
	}

	/**
	 * This method overrides the setXpath of base class
	 * 
	 * @param xpath
	 */
	private void setXpath(String xpath) {
		super.xpath = xpath;
	}

}
