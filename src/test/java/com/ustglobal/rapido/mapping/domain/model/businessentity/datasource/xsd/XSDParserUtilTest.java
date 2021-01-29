package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

public class XSDParserUtilTest {

	@Test
	public void getRootElement() throws ParserConfigurationException, SAXException, IOException {
		String rootElementName = XSDParserUtil.getRootElementName(
				ResourceUtils.getFile(this.getClass().getResource("/Sample.xsd")).getAbsolutePath());
	}

}
