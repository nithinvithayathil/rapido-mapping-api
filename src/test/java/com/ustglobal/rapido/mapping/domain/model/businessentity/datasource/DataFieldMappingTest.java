package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ustglobal.rapido.mapping.domain.model.businessentity.transformation.Transformation;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;

public class DataFieldMappingTest {
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void equalsShouldReturnTrueWhenObjectsHaveSameFieldMappingId() {
		String fieldMappingId = UUID.randomUUID().toString();
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		DataFieldMapping fieldMapping1 = new DataFieldMapping(fieldMappingId,
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		DataFieldMapping fieldMapping2 = new DataFieldMapping(fieldMappingId,
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		assertTrue(fieldMapping1.equals(fieldMapping2));
	}

	@Test
	public void equalsShouldReturnFalseWhenObjectsHaveDifferentFieldMappingId() {
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		DataFieldMapping fieldMapping1 = new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		DataFieldMapping fieldMapping2 = new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		assertFalse(fieldMapping1.equals(fieldMapping2));
	}

	@Test
	public void sameIdentityAsShouldReturnTrueWhenObjectsHaveSameFieldMappingId() {
		String fieldMappingId = UUID.randomUUID().toString();
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		DataFieldMapping fieldMapping1 = new DataFieldMapping(fieldMappingId,
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		DataFieldMapping fieldMapping2 = new DataFieldMapping(fieldMappingId,
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		assertTrue(fieldMapping1.sameIdentityAs(fieldMapping2));
	}

	@Test
	public void sameIdentityAsShouldReturnFalseWhenObjectsHaveDifferentFieldMappingId() {
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		DataFieldMapping fieldMapping1 = new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		DataFieldMapping fieldMapping2 = new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		assertFalse(fieldMapping1.sameIdentityAs(fieldMapping2));
	}

	@Test
	public void constructorShouldThrowExceptionWhenFieldMappingIdIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("FieldMappingId cannot be null or empty.");
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<>();
		sourceFieldIds.add(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		new DataFieldMapping(null, new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
				sourceFieldIds);
	}

	@Test
	public void constructorShouldThrowExceptionWhenFieldMappingIdIsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("FieldMappingId cannot be null or empty.");
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<>();
		sourceFieldIds.add(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		new DataFieldMapping("", new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()),
				sourceFieldIds);
	}

	@Test
	public void constructorShouldThrowExceptionWhenTargetFieldIdIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("TargetFieldId cannot be null or empty.");
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<>();
		sourceFieldIds.add(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		new DataFieldMapping(UUID.randomUUID().toString(), null, sourceFieldIds);
	}

	@Test
	public void constructorShouldThrowExceptionWhenSourceFieldIdsAreNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source fields required for non default mapping.");
		new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), null);
	}

	@Test
	public void constructorShouldThrowExceptionWhenSourceFieldIdisEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source fields required for non default mapping.");
		new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), new ArrayList<>());
	}
	
	@Test
	public void constructorShouldThrowExceptionWhenSourceFieldExistAndDefaultMappingExist() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source fields not allowed for default mapping.");
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		List<FieldDefinitionId> fieldDefinitionIds = new ArrayList<>();
		fieldDefinitionIds.add(fieldDefinitionId);
		new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), fieldDefinitionIds, true);
	}
	
	@Test
	public void constructorShouldThrowExceptionWhenSourceFieldNotExistAndDefaultMappingNotExist() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Source fields required for non default mapping.");
		new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), new ArrayList<>(),
				false);
	}

	@Test
	public void addSourceFieldValues() {
		DataFieldMapping dataFieldMapping = new DataFieldMapping();
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		dataFieldMapping.addSourceFields(fieldDefinitionId);
		assertTrue(dataFieldMapping.getSourceFieldIds().contains(fieldDefinitionId));
	}

	@Test
	public void removeSourceFieldValues() {
		FieldDefinitionId fieldDefinitionId1 = new FieldDefinitionId(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		FieldDefinitionId fieldDefinitionId2 = new FieldDefinitionId(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId1, fieldDefinitionId2));
		DataFieldMapping dataFieldMapping = new DataFieldMapping(UUID.randomUUID().toString(),
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		dataFieldMapping.removeSourceField(fieldDefinitionId2);
		assertTrue(!dataFieldMapping.getSourceFieldIds().contains(fieldDefinitionId2));
	}

	@Test
	public void addSourceTransformationsShouldThrowExceptionWhenFieldTransformationIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("FieldTransformation cannot be null or empty.");
		new DataFieldMapping().addSourceTransformations(null);
	}

	@Test
	public void addSourceTransformationsShouldThrowExceptionIfSourceFieldNotExistInMapping() {
		String fieldMappingId = UUID.randomUUID().toString();
		expectedException.expect(DomainException.class);
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId,
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()), sourceFieldIds);
		FieldTransformation fieldTransformation = new FieldTransformation(
				new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		fieldMapping.addSourceTransformations(fieldTransformation);
	}

	@Test
	public void addSourceTransformations() {
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, fieldDefinitionId, sourceFieldIds,
				new Transformation(), new ArrayList<>(), false);
		FieldTransformation fieldTransformation = new FieldTransformation(fieldDefinitionId);
		fieldMapping.addSourceTransformations(fieldTransformation);
		assertTrue(fieldMapping.getSourceTransformations().contains(fieldTransformation));
	}

	@Test
	public void removeSourceTransformationByFieldIdShouldThrowExceptionWhenFieldDefinitionIdIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("FieldDefinitionId cannot be null or empty.");
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId targetFieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(targetFieldDefinitionId));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, targetFieldDefinitionId, sourceFieldIds);
		fieldMapping.removeSourceTransformationByFieldId(null);
	}

	@Test
	public void removeSourceTransformationByFieldId() {
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(fieldDefinitionId));
		FieldTransformation fieldTransformation = new FieldTransformation(fieldDefinitionId);
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, fieldDefinitionId, sourceFieldIds, null,
				new ArrayList<FieldTransformation>(Arrays.asList(fieldTransformation)), false);
		fieldMapping.removeSourceTransformationByFieldId(fieldDefinitionId);
		assertTrue(!fieldMapping.getSourceTransformations().contains(fieldTransformation));
	}

	@Test
	public void assignCombineTransformShouldThrowExceptionWhenTransformationIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Transformation cannot be null or empty.");
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId targetFieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(targetFieldDefinitionId));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, targetFieldDefinitionId, sourceFieldIds,
				null, new ArrayList<>(), false);
		fieldMapping.assignCombineTransform(null);
	}

	@Test
	public void assignCombineTransform() {
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId targetFieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(targetFieldDefinitionId));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, targetFieldDefinitionId, sourceFieldIds);
		Transformation transformation = new Transformation(UUID.randomUUID().toString());
		fieldMapping.assignCombineTransform(transformation);
		assertTrue(fieldMapping.getCombinedTransformation().equals(transformation));
	}

	@Test
	public void removeCombineTransform() {
		String fieldMappingId = UUID.randomUUID().toString();
		FieldDefinitionId targetFieldDefinitionId = new FieldDefinitionId(fieldMappingId, UUID.randomUUID().toString());
		Collection<FieldDefinitionId> sourceFieldIds = new ArrayList<FieldDefinitionId>(
				Arrays.asList(targetFieldDefinitionId));
		DataFieldMapping fieldMapping = new DataFieldMapping(fieldMappingId, targetFieldDefinitionId, sourceFieldIds,
				new Transformation(UUID.randomUUID().toString()), new ArrayList<FieldTransformation>(), false);
		fieldMapping.removeCombineTransform();
	}

}
