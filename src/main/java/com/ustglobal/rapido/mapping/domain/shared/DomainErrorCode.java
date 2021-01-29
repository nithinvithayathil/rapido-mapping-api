package com.ustglobal.rapido.mapping.domain.shared;

public enum DomainErrorCode {

    PARAMETER_REQUIRED                  	(1001, "%s cannot be null or empty."),
    DATASOURCE_IMPORT_ERROR             	(1002, "Error while importing data source. Mostly related with file operations."),
    DATABASE_CONFIG_NOT_AVAILABLE       	(1003, "Database configuration for id %s which is required for the operation is not available"),
    BUSINESSENTITY_DOES_NOT_EXIST       	(1004, "The requested business entity with id %s and version %s does not exist."),
    SOURCE_FIELD_NOT_EXISTING_IN_MAPPING	(1005, "Source Field with identifier %s, not existing in mapping %s"),
    FIELD_NOT_EXISTING_IN_TARGET        	(1006, "Field with identifier %s, not existing in target %s"),
    FIELD_NOT_EXISTING_IN_SOURCE        	(1007, "Field with identifier %s, not existing in source %s"),
    TARGET_DOES_NOT_EXIST              	 	(1008, "The target with id %s does not exist in the business entity %s."),
    SOURCE_DOES_NOT_EXIST               	(1009, "The source with id %s does not exist in the business entity %s."),
    TARGET_FIELD_ALREADY_MAPPED         	(1010, "The target field with id %s already mapped in the map with id %s."),
    INVALID_ENTITY_VERSION              	(1011, "The entity version provided for %s is invalid."),
    SUGGESTED_VERSION_LOWER_THAN_LATEST 	(1012, "Cannot create new entity with version %s, as it is less than the latest version %s."),
    MAPPING_DOES_NOT_EXIST              	(1013, "Mapping with id %s does not exist on entity %s"),
    SOURCE_FIELD_ALREADY_MAPPED				(1014, "The source already mapped in mapping %s."),
    MAPPING_EXIST							(1015, "Mapping exist."),
    SOURCE_NOT_REQUIRED_FOR_DEFAULT_MAPPING	(1016, "Source fields not allowed for default mapping."),
    SOURCE_REQUIRED_FOR_NONDEFAULT_MAPPING	(1017, "Source fields required for non default mapping.");
	
	private final int code;
	private final String message;

	DomainErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
