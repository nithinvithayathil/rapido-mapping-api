package com.ustglobal.rapido.mapping.infrastructure.web;

import java.util.List;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;

public class SaveMappingCommand {

	private List<FieldDefinitionId> sources;
	private FieldDefinitionId target;
	private boolean isDefault;

	public List<FieldDefinitionId> getSources() {
		return sources;
	}

	public FieldDefinitionId getTarget() {
		return target;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

}
