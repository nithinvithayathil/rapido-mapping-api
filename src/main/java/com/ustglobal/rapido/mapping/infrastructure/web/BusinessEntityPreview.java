package com.ustglobal.rapido.mapping.infrastructure.web;

public class BusinessEntityPreview {

  private String id;
  private String version;
  private String name;
  private String description;
  private boolean isLocked;
  private boolean isBaseline;
  private boolean isLatest;
  private boolean hasTarget;
  private int sourceCount;
  private int mappingsCount;

  private BusinessEntityPreview(String id, String version, String name, String description,
      boolean isLocked,
      boolean isBaseline,
      boolean isLatest,
      boolean hasTarget,
      int sourceCount,
      int mappingsCount) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.description = description;
    this.isLocked = isLocked;
    this.isBaseline = isBaseline;
    this.isLatest = isLatest;
    this.hasTarget = hasTarget;
    this.sourceCount = sourceCount;
    this.mappingsCount = mappingsCount;
  }

  public String getId() {
    return id;
  }

  public String getVersion() {
    return version;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public boolean isBaseline() {
    return isBaseline;
  }

  public boolean isLatest() {
    return isLatest;
  }

  public boolean isHasTarget() {
    return hasTarget;
  }

  public int getSourceCount() {
    return sourceCount;
  }

  public int getMappingsCount() {
    return mappingsCount;
  }

  public static class Builder{

    private String id;
    private String version;
    private String name;
    private String description;
    private boolean isLocked;
    private boolean isBaseline;
    private boolean isLatest;
    private boolean hasTarget;
    private int sourceCount;
    private int mappingsCount;

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder isLocked(boolean isLocked){
      this.isLocked = isLocked;
      return this;
    }
    public Builder isBaseline(boolean isBaseline){
      this.isBaseline = isBaseline;
      return this;
    }
    public Builder isLatest(boolean isLatest){
      this.isLatest = isLatest;
      return this;
    }
    public Builder hasTarget(boolean hasTarget){
      this.hasTarget = hasTarget;
      return this;
    }

    public Builder sourceCount(int sourceCount){
      this.sourceCount = sourceCount;
      return this;
    }
    public Builder mappingsCount(int mappingsCount){
      this.mappingsCount = mappingsCount;
      return this;
    }

    public BusinessEntityPreview build(){
      return new BusinessEntityPreview(id, version, name, description, isLocked,
          isBaseline,isLatest,hasTarget, sourceCount, mappingsCount);
    }

  }
}
