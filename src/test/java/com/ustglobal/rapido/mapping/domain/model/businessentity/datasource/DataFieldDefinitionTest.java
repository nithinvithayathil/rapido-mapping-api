package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource;

import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.junit.Assert.*;

public class DataFieldDefinitionTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void equalsShouldReturnTrueWhenObjectsHaveSameFieldDefinitionId() {
		String  id = UUID.randomUUID().toString();
		String sourceDefinitionId = UUID.randomUUID().toString();
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(id, sourceDefinitionId);
		DataFieldDefinition fieldDefinition1 = new DataFieldDefinition(fieldDefinitionId, "fieldname");
		DataFieldDefinition fieldDefinition2 = new DataFieldDefinition(fieldDefinitionId, "fieldname2");
		assertTrue(fieldDefinition1.equals(fieldDefinition2));
	}

	@Test
	public void equalsShouldReturnFalseWhenObjectsHaveDifferentFieldDefinitionId() {
		FieldDefinitionId fieldDefinitionId1 = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		FieldDefinitionId fieldDefinitionId2 = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		DataFieldDefinition fieldDefinition1 = new DataFieldDefinition(fieldDefinitionId1, "fieldname");
		DataFieldDefinition fieldDefinition2 = new DataFieldDefinition(fieldDefinitionId2, "fieldname2");
		assertFalse(fieldDefinition1.equals(fieldDefinition2));
	}

	@Test
	public void sameIdentityAsShouldReturnTrueWhenObjectsHaveSameFieldDefinitionId() {
		String id = UUID.randomUUID().toString();
		String sourceDefinitionId = UUID.randomUUID().toString();
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(id, sourceDefinitionId);
		DataFieldDefinition fieldDefinition1 = new DataFieldDefinition(fieldDefinitionId, "fieldname");
		DataFieldDefinition fieldDefinition2 = new DataFieldDefinition(fieldDefinitionId, "fieldname2");
		assertTrue(fieldDefinition1.sameIdentityAs(fieldDefinition2));
	}

	@Test
	public void sameIdentityAsShouldReturnFalseWhenObjectsHaveDifferentFieldDefinitionId() {
		FieldDefinitionId fieldDefinitionId1 = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		FieldDefinitionId fieldDefinitionId2 = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		DataFieldDefinition fieldDefinition1 = new DataFieldDefinition(fieldDefinitionId1, "fieldname");
		DataFieldDefinition fieldDefinition2 = new DataFieldDefinition(fieldDefinitionId2, "fieldname2");
		assertFalse(fieldDefinition1.sameIdentityAs(fieldDefinition2));
	}

	@Test()
	public void constructorShouldThrowExceptionWhenFieldDefinitionIdIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("FieldDefinitionId cannot be null or empty.");
		new DataFieldDefinition(null, "field1");
	}

	@Test()
	public void constructorShouldThrowExceptionWhenNameIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Name cannot be null or empty.");
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		new DataFieldDefinition(fieldDefinitionId, null);
	}

	@Test()
	public void constructorShouldThrowExceptionWhenNameIsEmpty() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Name cannot be null or empty.");
		FieldDefinitionId fieldDefinitionId = new FieldDefinitionId(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		new DataFieldDefinition(fieldDefinitionId, "");
	}


}