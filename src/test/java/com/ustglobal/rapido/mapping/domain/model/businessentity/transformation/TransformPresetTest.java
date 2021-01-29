package com.ustglobal.rapido.mapping.domain.model.businessentity.transformation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TransformPresetTest {

	@Test
	public void equalsShouldReturnTrueWhenObjectsHaveSameKey() {
		TransformPreset transformPreset1 = new TransformPreset("key", "value1");
		TransformPreset transformPreset2 = new TransformPreset("key", "value2");
		assertTrue(transformPreset1.equals(transformPreset2));
	}

	@Test
	public void equalsShouldReturnFalseWhenObjectsHaveDifferentKey() {
		TransformPreset transformPreset1 = new TransformPreset("key1", "value1");
		TransformPreset transformPreset2 = new TransformPreset("key2", "value2");
		assertFalse(transformPreset1.equals(transformPreset2));
	}

}
