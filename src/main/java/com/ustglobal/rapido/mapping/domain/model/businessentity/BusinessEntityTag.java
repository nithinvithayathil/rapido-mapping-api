package com.ustglobal.rapido.mapping.domain.model.businessentity;

public enum BusinessEntityTag {
  BASELINE("baseline"), LATEST("latest"), LOCKED("locked"), NONE("none");

  private String textValue;

  BusinessEntityTag(String textValue){
    this.textValue = textValue;
  }

  public static BusinessEntityTag getByTextValue(String textValue){
    for (BusinessEntityTag type :
        BusinessEntityTag.values()) {
      if (type.textValue.equalsIgnoreCase(textValue)){
        return type;
      }
    }
    return BusinessEntityTag.NONE;
  }
}
