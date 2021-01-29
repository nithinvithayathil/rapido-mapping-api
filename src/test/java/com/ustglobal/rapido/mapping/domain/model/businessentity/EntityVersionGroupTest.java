package com.ustglobal.rapido.mapping.domain.model.businessentity;

import static org.junit.Assert.*;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldMapping;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.vdurmont.semver4j.Semver;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class EntityVersionGroupTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private String entityId = "id0b9cab39-e783-4683-b1f7-c08bccc7f473";

  @Test
  public void createNewVersionOfShouldCallRepositorySaveWithNewEntity() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    BusinessEntity entity = entityVersionGroup.createNewVersionOf("0.0.3", "1.0.0");

    assertTrue(entity.getId().getVersion().isEqualTo(new Semver("1.0.0")));
    assertTrue(entity.getId().getEntityId().equals(entityId));
    assertTrue(entity.getName().equals("n2"));
    assertTrue(entity.getDescription().equals("d2"));
    assertEquals(1, entity.getSources().size());
    assertEquals(1, entity.getMappings().size());
    assertNotNull(entity.getTarget());
  }

  @Test
  public void createNewVersionOfShouldCallCreateNextToLatestVersionWhenNoVersionSuggestionGiven() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    BusinessEntity entity = entityVersionGroup.createNewVersionOf("0.0.1", null);

    assertTrue(entity.getId().getVersion().isEqualTo(new Semver("0.0.4")));
    assertTrue(entity.getId().getEntityId().equals(entityId));
    assertTrue(entity.getName().equals("n1"));
    assertTrue(entity.getDescription().equals("d1"));
  }

  @Test
  public void createNewVersionOfShouldCallCreateNextToLatestVersionWhenNoVersionSuggestionGivenAndUnOrderedEntityList() {
    List<BusinessEntity> entities = getListOfEntities();
    entities.add(new BusinessEntity(
        new BusinessEntityId(entityId, "0.0.2"), "N3", "D3"));
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, entities);
    BusinessEntity entity = entityVersionGroup.createNewVersionOf("0.0.1", null);

    assertTrue(entity.getId().getVersion().isEqualTo(new Semver("0.0.4")));
    assertTrue(entity.getId().getEntityId().equals(entityId));
    assertTrue(entity.getName().equals("n1"));
    assertTrue(entity.getDescription().equals("d1"));
  }

  @Test
  public void createNewVersionOfShouldThrowExceptionWhenFromVersionNotFound() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The requested business entity with id " + entityId + " and version 0.0.2 does not exist.");
    entityVersionGroup.createNewVersionOf("0.0.2", "1.0.0");
  }

  @Test
  public void createNewVersionOfShouldThrowExceptionWhenFromVersionIncorrectFormat() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The entity version provided for fromVersion is invalid.");
    entityVersionGroup.createNewVersionOf("0.0.df", "1.0.0");
  }

  @Test
  public void createNewVersionOfShouldThrowExceptionWhenSuggestedVersionIncorrectFormat() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The entity version provided for newSuggestedVersion is invalid.");
    entityVersionGroup.createNewVersionOf("0.0.3", "10.0");
  }

  @Test
  public void costructorShouldThrowExceptionWhenEntityIdIsNotProvided() {
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("EntityId cannot be null or empty.");
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup("", getListOfEntities());
  }

  @Test
  public void createNewVersionOfShouldThrowExceptionWhenFromVersionIsNotProvided() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("FromVersion cannot be null or empty.");
    entityVersionGroup.createNewVersionOf("", "10.0");
  }

  @Test
  public void createNewVersionOfShouldThrowExceptionWhenSuggestedVersionIsLesserThanTheLatest() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("Cannot create new entity with version 0.0.2, as it is less than the latest version 0.0.3.");
    entityVersionGroup.createNewVersionOf("0.0.1", "0.0.2");
  }

  @Test
  public void createNewVersionOfShouldTagTheNewVersionWithLatest() {
    List<BusinessEntity> entities =  getListOfEntities();
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, entities);
    BusinessEntity newEntity = entityVersionGroup.createNewVersionOf("0.0.1", "1.0.0");
    assertTrue(newEntity.isLatest());
    assertFalse(entities.get(0).isLatest());
    assertFalse(entities.get(1).isLatest());
  }

  @Test
  public void markAsLatestShouldThrowExceptionWhenGivenVersionIncorrectFormat() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The entity version provided for version is invalid.");
    entityVersionGroup.markAsLatest("10.0");
  }

  @Test
  public void markAsLatestShouldThrowExceptionWhenGivenVersionNotProvided() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("version cannot be null or empty.");
    entityVersionGroup.markAsLatest("");
  }

  @Test
  public void markAsBaselineShouldThrowExceptionWhenGivenVersionIncorrectFormat() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The entity version provided for version is invalid.");
    entityVersionGroup.markAsBaseline("10.0");
  }

  @Test
  public void markAsBaselineShouldThrowExceptionWhenGivenVersionNotProvided() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("version cannot be null or empty.");
    entityVersionGroup.markAsBaseline("");
  }

  @Test
  public void markAsBaselineShouldAddTagToTheGivenVersionBusinessEntity()
      throws IllegalAccessException {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    entityVersionGroup.markAsBaseline("0.0.1");
    BusinessEntity entity = entityVersionGroup.findEntityWithVersion("0.0.1");
    List<BusinessEntityTag> tags = (List<BusinessEntityTag>) FieldUtils.readField(entity, "tags", true);
    assertTrue(tags.contains(BusinessEntityTag.BASELINE));
  }

  @Test
  public void markAsBaselineShouldMarkAllLowerVersionsWithLockedTag()
      throws IllegalAccessException {
    List<BusinessEntity> entities = getListOfEntities();
    entities.add(new BusinessEntity(
        new BusinessEntityId(entityId, "0.0.2"), "N3", "D3"));
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, entities);
    entityVersionGroup.markAsBaseline("0.0.3");
    BusinessEntity entity = entityVersionGroup.findEntityWithVersion("0.0.1");
    List<BusinessEntityTag> tags = (List<BusinessEntityTag>) FieldUtils.readField(entity, "tags", true);
    assertTrue(tags.contains(BusinessEntityTag.LOCKED));
    entity = entityVersionGroup.findEntityWithVersion("0.0.2");
    tags = (List<BusinessEntityTag>) FieldUtils.readField(entity, "tags", true);
    assertTrue(tags.contains(BusinessEntityTag.LOCKED));
  }

  @Test
  public void markAsBaselineShouldNotHaveGivenVersionTaggedWithLocked()
      throws IllegalAccessException {
    List<BusinessEntity> entities = getListOfEntities();
    entities.add(new BusinessEntity(
        new BusinessEntityId(entityId, "0.0.2"), "N3", "D3"));
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, entities);
    entityVersionGroup.markAsBaseline("0.0.3");
    BusinessEntity entity = entityVersionGroup.findEntityWithVersion("0.0.3");
    List<BusinessEntityTag> tags = (List<BusinessEntityTag>) FieldUtils.readField(entity, "tags", true);
    assertTrue(!tags.contains(BusinessEntityTag.LOCKED));
  }

  @Test
  public void markAsLatestShouldAddTagToTheGivenVersionBusinessEntity()
      throws IllegalAccessException {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    entityVersionGroup.markAsLatest("0.0.1");
    BusinessEntity entity = entityVersionGroup.findEntityWithVersion("0.0.1");
    List<BusinessEntityTag> tags = (List<BusinessEntityTag>) FieldUtils.readField(entity, "tags", true);
    assertTrue(tags.contains(BusinessEntityTag.LATEST));
  }


  @Test
  public void equals() {
    EntityVersionGroup entityVersionGroup = new EntityVersionGroup(entityId, getListOfEntities());
    EntityVersionGroup entityVersionGroup2 = new EntityVersionGroup(entityId, getListOfEntities());
    assertTrue(entityVersionGroup.equals(entityVersionGroup2));
  }

  private List<BusinessEntity> getListOfEntities() {
    List<BusinessEntity> entities = new ArrayList<>();
    String sourceId = UUID.randomUUID().toString();
    String targetId = UUID.randomUUID().toString();
    String mappingId = UUID.randomUUID().toString();
    String fieldId = UUID.randomUUID().toString();

    entities.add(getEntityWithoutMapping("0.0.1", "n1", "d1"));
    BusinessEntity entity2 = getEntityWithMapping(
        "0.0.3", "n2", "d2",
        sourceId, targetId, mappingId, fieldId);
    entities.add(entity2);

    return entities;
  }

  private BusinessEntity getEntityWithMapping(
      String version, String name, String desc,
      String sourceId, String targetId, String mappingId,
      String fieldId) {
    BusinessEntity entity2 = getEntityWithoutMapping(version, name, desc);
    DataSourceDefinition sourceDefinition = new DataSourceDefinition(sourceId, "S1");
    sourceDefinition.addField(new DataFieldDefinition(new FieldDefinitionId(fieldId, sourceId), "SF1"));
    entity2.addSource(sourceDefinition);

    DataSourceDefinition targetDefinition = new DataSourceDefinition(targetId, "T1");
    targetDefinition.addField(new DataFieldDefinition(new FieldDefinitionId(fieldId, targetId), "TF1"));
    entity2.assignTarget(targetDefinition);

    List<FieldDefinitionId> sourceIds = new ArrayList<>();
    sourceIds.add(new FieldDefinitionId(fieldId, sourceId));
    entity2.addMapping(new DataFieldMapping(mappingId, new FieldDefinitionId(fieldId, targetId), sourceIds));
    return entity2;
  }

  private BusinessEntity getEntityWithoutMapping(String version, String name, String desc) {
    return new BusinessEntity(new BusinessEntityId(entityId, version), name, desc);
  }


}