package com.ustglobal.rapido.mapping.domain.model.businessentity.transformation;

import com.ustglobal.rapido.mapping.domain.shared.ValueObject;

public class TransformPreset implements ValueObject<TransformPreset> {

	private static final long serialVersionUID = 7601854239848686215L;

	private String key;
	private String value;

	public TransformPreset(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean sameValueAs(TransformPreset other) {
		return other != null && this.key.equalsIgnoreCase(other.key);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TransformPreset that = (TransformPreset) o;
		return sameValueAs(that);
	}

}