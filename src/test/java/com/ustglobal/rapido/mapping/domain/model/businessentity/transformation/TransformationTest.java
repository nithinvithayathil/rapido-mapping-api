package com.ustglobal.rapido.mapping.domain.model.businessentity.transformation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ustglobal.rapido.mapping.domain.shared.DomainException;

public class TransformationTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void equalsShouldReturnTrueWhenObjectsHaveSameTransformationId() {
		String transformationId = UUID.randomUUID().toString();
		Transformation transformation1 = new Transformation(transformationId);
		Transformation transformation2 = new Transformation(transformationId);
		assertTrue(transformation1.equals(transformation2));
	}

	@Test
	public void equalsShouldReturnFalseWhenObjectsHaveDifferentTransformationId() {
		Transformation transformation1 = new Transformation(UUID.randomUUID().toString());
		Transformation transformation2 = new Transformation(UUID.randomUUID().toString());
		assertFalse(transformation1.equals(transformation2));
	}

	@Test
	public void addTransformPresetValuesShouldThrowExceptionWhenPresetKeyIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("PresetKey cannot be null or empty.");
		new Transformation().addTransformPresetValues(null, "PresetValue");
	}

	@Test
	public void addTransformPresetValuesShouldThrowExceptionWhenPresetValueIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("PresetValue cannot be null or empty.");
		new Transformation().addTransformPresetValues("PresetKey", null);
	}

	@Test
	public void addTransformPresetValues() {
		String transformationId = UUID.randomUUID().toString();
		Transformation transformation = new Transformation(transformationId);
		transformation.addTransformPresetValues("PresetKey", "PresetValue");
		assertTrue(transformation.getTransformPresetValues().containsKey("PresetKey"));
		assertTrue(transformation.getTransformationId().equals(transformationId));
		assertFalse(transformation.hasNonPersistedChanges());
	}

	@Test
	public void removeSourceTransformationsShouldThrowExceptionWhenPresetKeyIsNull() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("PresetKey cannot be null or empty.");
		new Transformation().removeSourceTransformations(null);
	}

	@Test
	public void removeSourceTransformations() {
		Map<String, String> transformPresetValues = new HashMap<>();
		transformPresetValues.put("PresetKey", "PresetValue");
		Transformation transformation = new Transformation(UUID.randomUUID().toString(), transformPresetValues);
		transformation.removeSourceTransformations("PresetKey");
		assertTrue(!transformation.getTransformPresetValues().containsKey("PresetKey"));
	}

}
