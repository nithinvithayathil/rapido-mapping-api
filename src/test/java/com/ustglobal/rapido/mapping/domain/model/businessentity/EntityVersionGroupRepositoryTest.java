package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldMapping;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntityVersionGroupRepositoryTest {

  @Mock
  private BusinessEntityRepository repositoryMock;

  @InjectMocks
  private EntityVersionGroupRepository groupRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private String entityId = "id0b9cab39-e783-4683-b1f7-c08bccc7f473";

  @Test
  public void findByIdCallsfindBusinessEntitiesWithIdInBusinessEntityRepo()
      throws IllegalAccessException {
    Mockito.when(repositoryMock.findBusinessEntitiesWithId(entityId)).thenReturn(getListOfEntities());
    groupRepository.findById(entityId);
    Mockito.verify(repositoryMock).findBusinessEntitiesWithId(entityId);
  }

  @Test
  public void saveCallsSaveInBusinessEntityRepoforEachOfTheEntitiesWhichHasChange()
      throws IllegalAccessException {
    Mockito.when(repositoryMock.findBusinessEntitiesWithId(entityId)).thenReturn(getListOfEntities());
    EntityVersionGroup entityVersionGroup = groupRepository.findById(entityId);
    entityVersionGroup.markAsLatest("0.0.1");
    groupRepository.save(entityVersionGroup);
    Mockito.verify(repositoryMock,Mockito.times(1)).save(Mockito.any());
  }

  @Test
  public void findByIdThrowsExceptionIfIdNotFound(){
    expectedException.expect(DomainException.class);
    expectedException.expectMessage("The requested business entity with id 1234 and version any does not exist.");
    groupRepository.findById("1234");
  }

  private List<BusinessEntity> getListOfEntities() throws IllegalAccessException {
    List<BusinessEntity> entities = new ArrayList<>();
    String sourceId = UUID.randomUUID().toString();
    String targetId = UUID.randomUUID().toString();
    String mappingId = UUID.randomUUID().toString();
    String fieldId = UUID.randomUUID().toString();

    entities.add(getEntityWithoutMapping("0.0.1", "n1", "d1"));

    BusinessEntity entity2 = getEntityWithMapping(
        "0.0.3", "n2", "d2",
        sourceId, targetId, mappingId, fieldId);
    FieldUtils.writeDeclaredField(entity2, "hasChanges", false, true);
    entities.add(entity2);

    return entities;
  }

  private BusinessEntity getEntityWithMapping(
      String version, String name, String desc,
      String sourceId, String targetId, String mappingId,
      String fieldId) throws IllegalAccessException {
    BusinessEntity entity2 = getEntityWithoutMapping(version, name, desc);
    DataSourceDefinition sourceDefinition = new DataSourceDefinition(sourceId, "S1");
    sourceDefinition.addField(new DataFieldDefinition(new FieldDefinitionId(fieldId, sourceId), "SF1"));
    FieldUtils.writeDeclaredField(sourceDefinition, "hasChanges", false, true);
    entity2.addSource(sourceDefinition);

    DataSourceDefinition targetDefinition = new DataSourceDefinition(targetId, "T1");
    targetDefinition.addField(new DataFieldDefinition(new FieldDefinitionId(fieldId, targetId), "TF1"));
    FieldUtils.writeDeclaredField(targetDefinition, "hasChanges", false, true);
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