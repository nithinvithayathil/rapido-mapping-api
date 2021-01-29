package com.ustglobal.rapido.mapping.domain.model.businessentity;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.vdurmont.semver4j.Semver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataFieldMapping;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinition;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.FieldDefinitionId;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;

public class BusinessEntityTest {

	public static final String BUSINESS_ENTITY_NAME = "BusinessEntityTest";

	public static final String BUSINESS_ENTITY_DESCRIPTION = "Unit test for business entity";

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void equalsShouldReturnTrueWhenObjectsHaveSameBusinessEntityId() {
		BusinessEntityId businessEntityId = new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1");
		BusinessEntity businessEntity1 = new BusinessEntity(businessEntityId, BUSINESS_ENTITY_NAME,
				BUSINESS_ENTITY_DESCRIPTION);
		BusinessEntity businessEntity2 = new BusinessEntity(businessEntityId, BUSINESS_ENTITY_NAME,
				BUSINESS_ENTITY_DESCRIPTION);
		assertTrue(businessEntity1.equals(businessEntity2));
	}

	@Test
	public void equalsShouldReturnFalseWhenObjectsHaveDifferentBusinessEntityId() {
		BusinessEntity businessEntity1 = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		BusinessEntity businessEntity2 = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		assertFalse(businessEntity1.equals(businessEntity2));
	}

	@Test
	public void constructorShouldThrowExceptionWhenBusinessEntityIdIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("BusinessEntityId cannot be null or empty.");
		new BusinessEntity(null, BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
	}

	@Test
	public void constructorShouldThrowExceptionWhenNameIsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Name cannot be null or empty.");
		new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"), "",
				BUSINESS_ENTITY_DESCRIPTION);
	}

	@Test
	public void addSource_throw_exception_when_source_IsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.addSource(null);
	}

	@Test
	public void addSource() {
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		DataSourceDefinition dataSourceDefinition = new DataSourceDefinition(UUID.randomUUID().toString(),
				"DataSourceName");
		businessEntity.addSource(dataSourceDefinition);
		assertTrue(businessEntity.getSources().contains(dataSourceDefinition));
	}

	@Test
	public void removeSource_throw_exception_when_source_IsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.removeSource(null);
	}

	@Test
	public void removeSource() {
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		DataSourceDefinition dataSourceDefinition = new DataSourceDefinition(UUID.randomUUID().toString(),
				"DataSourceName");
		businessEntity.addSource(dataSourceDefinition);
		businessEntity.removeSource(dataSourceDefinition);
		assertTrue(!businessEntity.getSources().contains(dataSourceDefinition));
	}

	@Test
	public void removeSourceById() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		String mappingSourceDefinitionId = UUID.randomUUID().toString();
				BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName"));
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), mappingSourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(mappingSourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(mappingSourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeSourceById(sourceDefinitionId);
		assertTrue(!businessEntity.getSources().contains(sourceDefinitionId));
	}
	
	@Test
	public void removeSourceById_throw_exception_when_source_is_already_mapped() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		expectedException.expect(DomainException.class);
		expectedException
				.expectMessage("The source already mapped in mapping " + dataFieldMapping.getFieldMappingId() + ".");
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeSourceById(sourceDefinitionId);
	}

	@Test
	public void findDataSourceDefinitionById_throw_exception_when_id_isEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Id cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.findDataSourceDefinitionById(null);
	}

	@Test
	public void findDataSourceDefinitionById_return_null_if_sourceList_isEmpty() {
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		assertNull(businessEntity.findDataSourceDefinitionById(UUID.randomUUID().toString()));
	}

	@Test
	public void findDataSourceDefinitionById_return_datasourcedefinition_if_sourceList_have_id() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName"));
		DataSourceDefinition dataSourceDefinition = businessEntity.findDataSourceDefinitionById(sourceDefinitionId);
		assertTrue(sourceDefinitionId.equals(dataSourceDefinition.getSourceDefinitionId()));
	}

	@Test
	public void addMapping_throw_exception_when_dataFieldMapping_isNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("DataFieldMapping cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.addMapping(null);
	}

	@Test
	public void addMapping_throw_exception_when_target_does_not_exist_in_target() {
		expectedException.expect(DomainException.class);
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity
				.assignTarget(new DataSourceDefinition(UUID.randomUUID().toString(), "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
	}

	@Test
	public void addMapping() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		assertTrue(businessEntity.getMappings().contains(dataFieldMapping));
	}

	@Test
	public void removeMapping() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeMapping(dataFieldMapping);
		assertTrue(!businessEntity.getMappings().contains(dataFieldMapping));
	}

	@Test
	public void removeMappingById() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		String fieldMappingId = UUID.randomUUID().toString();
		DataFieldMapping dataFieldMapping = new DataFieldMapping(fieldMappingId, fieldDefinitionId, sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeMappingById(fieldMappingId);
		assertTrue(!businessEntity.getMappings().contains(dataFieldMapping));
	}

	@Test
	public void removeAllMapping() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeAllMapping();
		assertTrue(!businessEntity.getMappings().contains(dataFieldMapping));
	}

	@Test
	public void removeTarget() {
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName"));
		businessEntity.removeTarget();
		assertNull(businessEntity.getTarget());
	}
	
	@Test
	public void removeTarget_throw_exception_if_mapping_exist() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Mapping exist.");
		String sourceDefinitionId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), sourceDefinitionId);
		Collection<DataFieldDefinition> dataFields = new ArrayList<DataFieldDefinition>(
				Arrays.asList(new DataFieldDefinition(fieldDefinitionId, "FieldName")));
		businessEntity.addSource(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		businessEntity.assignTarget(new DataSourceDefinition(sourceDefinitionId, "DataSourceName", dataFields));
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(), fieldDefinitionId,
				sourceFieldIds);
		businessEntity.addMapping(dataFieldMapping);
		businessEntity.removeTarget();
	}

	@Test
	public void changeName_throw_exception_when_newName_isEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Name cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.changeName("");
	}
	
	@Test
	public void changeName() {
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.changeName("New BusinessEntity Name");
	}
	
	@Test
	public void changeDescription_throw_exception_when_newName_isEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Description cannot be null or empty.");
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.changeDescription("");
	}
	
	@Test
	public void changeDescription() {
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(UUID.randomUUID().toString(), "0.0.1"),
				BUSINESS_ENTITY_NAME, BUSINESS_ENTITY_DESCRIPTION);
		businessEntity.changeDescription("New BusinessEntity Description");
	}

	@Test
	public void test_cloneWithVersion__creates_object_with_given_version(){
		String entityId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
		BusinessEntity clonedEntity = businessEntity.cloneWithNewVersion("1.0.0");
		assertTrue(clonedEntity.getId().getVersion().isEqualTo(new Semver("1.0.0")));
	}

	@Test
	public void test_cloneWithVersion__object_returned_not_equal_to_current_object(){
		String entityId = UUID.randomUUID().toString();
		BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
		BusinessEntity clonedEntity = businessEntity.cloneWithNewVersion("1.0.0");
		assertFalse(clonedEntity.equals(businessEntity));
	}

	@Test
	public void addTag_changes_entity_if_tag_is_not_present(){
    String entityId = UUID.randomUUID().toString();
    BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
    businessEntity.addTag(BusinessEntityTag.LATEST);
    assertTrue(businessEntity.hasNonPersistedChanges());
  }

  @Test
  public void removeTag_does_not_change_entity_if_tag_is_not_present(){
    String entityId = UUID.randomUUID().toString();
    BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
    businessEntity.removeTag(BusinessEntityTag.LATEST);
    assertFalse(businessEntity.hasNonPersistedChanges());
  }

  @Test
  public void removeTag_changes_entity_if_tag_is_present() throws IllegalAccessException {
    String entityId = UUID.randomUUID().toString();
    BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
    businessEntity.addTag(BusinessEntityTag.LATEST);
    FieldUtils.writeDeclaredField(businessEntity, "hasChanges", false, true);
    businessEntity.removeTag(BusinessEntityTag.LATEST);
    assertTrue(businessEntity.hasNonPersistedChanges());
  }

  @Test
  public void addTag_adds_appropriate_tags_to_entity(){
    String entityId = UUID.randomUUID().toString();
    BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
    businessEntity.addTag(BusinessEntityTag.LATEST);
    businessEntity.addTag(BusinessEntityTag.BASELINE);
    businessEntity.addTag(BusinessEntityTag.LOCKED);
    assertTrue(businessEntity.isLocked());
    assertTrue(businessEntity.isBaseline());
    assertTrue(businessEntity.isLatest());
  }

  @Test
  public void test_entity_without_any_tag(){
    String entityId = UUID.randomUUID().toString();
    BusinessEntity businessEntity = new BusinessEntity(new BusinessEntityId(entityId, "0.0.1"), "entity1", "entitydescription1");
    assertFalse(businessEntity.isLocked());
    assertFalse(businessEntity.isBaseline());
    assertFalse(businessEntity.isLatest());
  }

}
