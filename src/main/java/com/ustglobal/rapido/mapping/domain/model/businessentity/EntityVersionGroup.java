package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.shared.*;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * Represents a Group of BusinessEntities with different versions but same entityId.
 * Abstracts functionalities applied on all version of BusinessEntities.
 */
public class EntityVersionGroup implements Entity<EntityVersionGroup> {

  private String entityId;
  private List<BusinessEntity> sortedVersionEntities;

  public EntityVersionGroup(String entityId, List<BusinessEntity> allVersionEntities) {
    DomainValidator.notEmpty(entityId, DomainErrorCode.PARAMETER_REQUIRED, "EntityId");
    DomainValidator.notEmpty(allVersionEntities, DomainErrorCode.BUSINESSENTITY_DOES_NOT_EXIST, entityId, "any");
    this.entityId = entityId;
    this.sortedVersionEntities = sortEntitiesByVersionInChronologicalOrder(allVersionEntities);
  }

  public BusinessEntity createNewVersionOf(String fromVersion, String newSuggestedVersion){
    DomainValidator.notEmpty(fromVersion, DomainErrorCode.PARAMETER_REQUIRED, "FromVersion");
    BusinessEntity fromEntity = findEntityWithVersion(fromVersion, "fromVersion");
    DomainValidator.notNull(fromEntity, DomainErrorCode.BUSINESSENTITY_DOES_NOT_EXIST, entityId, fromVersion);

    Semver latestVersionOfEntity = findLatestVersionForEntity();
    Semver newVersion = getNextMinorVersionFromLatest();

    if(!StringUtils.isEmpty(newSuggestedVersion)) {
      Semver suggestedVersion = getSemverFromString(newSuggestedVersion, "newSuggestedVersion");
      if(isSuggestedVersionGreaterthanLatest(latestVersionOfEntity, suggestedVersion)) {
        newVersion = suggestedVersion;
      }
    }

    BusinessEntity newEntity = fromEntity.cloneWithNewVersion(newVersion.toString());
    sortedVersionEntities.add(newEntity);
    markAsLatest(newEntity.getId().getVersion().toString());
    return newEntity;
  }

  private boolean isSuggestedVersionGreaterthanLatest(Semver latestVersionOfEntity, Semver suggestedVersion) {
    if (latestVersionOfEntity.isGreaterThanOrEqualTo(suggestedVersion)) {
      throw new DomainException(DomainErrorCode.SUGGESTED_VERSION_LOWER_THAN_LATEST, null,
          suggestedVersion.toString(), latestVersionOfEntity.toString());
    }
    return true;
  }

  public void markAsBaseline(String version){
    DomainValidator.notEmpty(version, DomainErrorCode.PARAMETER_REQUIRED, "version");
    BusinessEntity taggedEntity = addTagToVersion(version, BusinessEntityTag.BASELINE);
    lockVersionsBelowTheGivenEtity(taggedEntity);
  }

  public String getEntityId() {
    return entityId;
  }

  @Override
  public boolean sameIdentityAs(EntityVersionGroup other) {
    return other != null && this.entityId.equals(other.entityId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    EntityVersionGroup that = (EntityVersionGroup) o;

    return sameIdentityAs(that);
  }

  void markAsLatest(String version){
    DomainValidator.notEmpty(version, DomainErrorCode.PARAMETER_REQUIRED, "version");
    addTagToVersion(version, BusinessEntityTag.LATEST);
  }

  void save(BusinessEntityRepository entityRepository){
    for (BusinessEntity entity : this.sortedVersionEntities) {
      if(entity.hasNonPersistedChanges()){
        entityRepository.save(entity);
      }
    }
  }

  private void lockVersionsBelowTheGivenEtity(BusinessEntity taggedEntity) {
    int indexOfTaggedEntity = sortedVersionEntities.indexOf(taggedEntity);
    for (int index = indexOfTaggedEntity + 1; index < sortedVersionEntities.size(); index++){
      sortedVersionEntities.get(index).addTag(BusinessEntityTag.LOCKED);
    }
  }

  private BusinessEntity addTagToVersion(String version, BusinessEntityTag tag) {
    BusinessEntity foundEntity = findEntityWithVersion(version, "version");
    DomainValidator
        .notNull(foundEntity, DomainErrorCode.BUSINESSENTITY_DOES_NOT_EXIST, entityId, version);
    removeGivenTagFromAllVersions(tag);
    foundEntity.addTag(tag);
    return foundEntity;
  }

  private void removeGivenTagFromAllVersions(BusinessEntityTag tag) {
    this.sortedVersionEntities.forEach(entity ->{
      entity.removeTag(tag);
    });
  }

  private Semver getLatestVersionOf(String newSuggestedVersion, Semver newVersion) {
    if(!StringUtils.isEmpty(newSuggestedVersion)){
      Semver suggestedVersion = getSemverFromString(newSuggestedVersion, "newSuggestedVersion");
      if(suggestedVersion.isGreaterThan(newVersion)){
        newVersion = suggestedVersion;
      }
    }
    return newVersion;
  }

  private Semver getNextMinorVersionFromLatest() {
    Semver latestVersion = findLatestVersionForEntity();
    return latestVersion.nextPatch();
  }

  public BusinessEntity findEntityWithVersion(String version){
    return findEntityWithVersion(version, "version");
  }

  /**
   * Finds a given business entity from all available versions. Throws DomainException when the version
   * provided is not in correct format.
   * @param version Version to find
   * @param versionFieldName Name of the version field being searched. Helps to get proper validation
   * error when non appropriate version is passed in.
   * @return Business entity in the given version
   */
  private BusinessEntity findEntityWithVersion(String version, String versionFieldName) {
    BusinessEntity fromEntity = null;
    for (BusinessEntity entity : sortedVersionEntities) {
      if (isEntityOfVersion(entity, getSemverFromString(version, versionFieldName))){
        fromEntity = entity;
        break;
      }
    }
    return fromEntity;
  }

  private boolean isEntityOfVersion(BusinessEntity entity, Semver version) {
    return entity.getId().getVersion().isEqualTo(version);
  }

  private Semver getSemverFromString(String version, String fieldName){
    try {
      return new Semver(version);
    }
    catch (SemverException ex){
      throw new DomainException(DomainErrorCode.INVALID_ENTITY_VERSION, ex, fieldName);
    }
  }

  private Semver findLatestVersionForEntity(){
    return sortedVersionEntities.get(0).getId().getVersion();
  }

  private List<BusinessEntity> sortEntitiesByVersionInChronologicalOrder(List<BusinessEntity> allVersionEntities) {
    allVersionEntities.sort((entity1, entity2)->{
      if(isEntityOfVersion(entity1, entity2.getId().getVersion())){
        return 0;
      }
      if(entity1.getId().getVersion().isLowerThan(entity2.getId().getVersion())){
        return 1;
      }
      else {
        return -1;
      }
    });
    return allVersionEntities;
  }

}
