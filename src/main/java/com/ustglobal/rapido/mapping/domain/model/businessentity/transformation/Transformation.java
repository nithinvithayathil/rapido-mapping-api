package com.ustglobal.rapido.mapping.domain.model.businessentity.transformation;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import com.ustglobal.rapido.mapping.domain.shared.Persistable;
import com.ustglobal.rapido.mapping.domain.shared.ValueObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Transient;

/**
 * Represents the transformation in Rapido. It stores a transformation script
 * along with other details. Also has a list of transform presets which can be
 * given a value during mapping definition.
 */
public class Transformation implements ValueObject<Transformation>, Persistable {
	
	private static final long serialVersionUID = -2174251842450336982L;
	
	private String transformationId;
	private Map<String, String> transformPresetValues;
	@Transient
	private boolean hasChanges;

	public Transformation(String transformationId, Map<String, String> transformPresetValues) {
		this.transformationId = transformationId;
		this.transformPresetValues = transformPresetValues;
	}

	public Transformation(String transformationId) {
		this(transformationId, null);
	}

	public Transformation() {
	}

	public void addTransformPresetValues(String presetKey, String presetValue)
			throws DomainException {
		DomainValidator.notEmpty(presetKey, DomainErrorCode.PARAMETER_REQUIRED, "PresetKey");
		DomainValidator.notEmpty(presetValue, DomainErrorCode.PARAMETER_REQUIRED, "PresetValue");
		if (this.transformPresetValues == null) {
			transformPresetValues = new HashMap<>();
		}
		transformPresetValues.put(presetKey, presetValue);
	}

	public void removeSourceTransformations(String presetKey) throws DomainException {
		DomainValidator.notEmpty(presetKey, DomainErrorCode.PARAMETER_REQUIRED, "PresetKey");
		if (this.transformPresetValues != null) {
			transformPresetValues.remove(presetKey);
		}
	}

	@Override
	public boolean sameValueAs(Transformation other) {
		return other != null && other.transformationId.equals(this.transformationId);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Transformation that = (Transformation) o;
		return sameValueAs(that);
	}

	public String getTransformationId() {
		return transformationId;
	}

	public Map<String, String> getTransformPresetValues() {
		if(transformPresetValues == null){
			return null;
		}
		return Collections.unmodifiableMap(transformPresetValues);
	}

	@Override
	public boolean hasNonPersistedChanges() {
		return hasChanges;
	}
}
