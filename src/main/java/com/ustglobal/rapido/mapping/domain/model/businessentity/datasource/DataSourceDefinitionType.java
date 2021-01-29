package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

/**
 * Represents the different types of datasource definitions that can be added to the business entity
 */
public enum DataSourceDefinitionType {
    XSD("XSD"), COBOL("COBOL"), DATABASE("DATABASE"), NONE("none");

    private String textValue;

    DataSourceDefinitionType(String textValue){
      this.textValue = textValue;
    }

    public static DataSourceDefinitionType getByTextValue(String textValue){
      for (DataSourceDefinitionType type :
          DataSourceDefinitionType.values()) {
        if (type.textValue.equalsIgnoreCase(textValue)){
          return type;
        }
      }
      return DataSourceDefinitionType.NONE;
    }
}
