package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import java.util.Arrays;
import java.util.List;

public class XSDDataTypeValidator {

	static List<String> xsdDataTypes = Arrays.asList("AnyType", "Integer", "Date", "Decimal", "DateTime", "Time",
			"Long", "Short", "Boolean", "String", "NormalizedString");

	public static boolean isDefaultType(String type) {
		return xsdDataTypes.stream().anyMatch(c -> c.equalsIgnoreCase(type));
	}

}
