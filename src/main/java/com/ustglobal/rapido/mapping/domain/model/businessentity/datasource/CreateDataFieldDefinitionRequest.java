package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

public class CreateDataFieldDefinitionRequest {

	private FieldDefinitionId fieldDefinitionId;
	private String fieldName;
	private String xpath;

	public CreateDataFieldDefinitionRequest(FieldDefinitionId fieldDefinitionId, String fieldName) {

		this.fieldDefinitionId = fieldDefinitionId;
		this.fieldName = fieldName;
	}

	public FieldDefinitionId getFieldDefinitionId() {
		return fieldDefinitionId;
	}

	public String getFieldName() {
		return fieldName;
	}

	/**
	 * This method is used to over ride the default xpath value in Datafield
	 * definition
	 * 
	 * @param xpath
	 */
	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getXpath() {
		return xpath;
	}
}
