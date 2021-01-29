package com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.xsd;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ResourceUtils;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceDefinitionType;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.DataSourceFactoryProvider;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromDBMetaRequest;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.ParseFromFileRequest;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;

@RunWith(MockitoJUnitRunner.class)
public class XSDFileDataSourceDefinitionParserTest {

	@InjectMocks
	private XSDFileDataSourceDefinitionParser xsdFileDataSourceDefinitionParser;

	@Mock
	private DataSourceFactoryProvider dataSourceFactoryProvider;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void parse_throws_exception_when_parserequest_is_not_an_instanceof_ParseFromFileRequest() {
		expectedException.expect(DomainException.class);
		expectedException.expectMessage("Error while importing data source. Mostly related with file operations.");
		xsdFileDataSourceDefinitionParser.parse(new ParseFromDBMetaRequest(UUID.randomUUID().toString()));
	}

	@Test
	public void parse() throws IOException {
		when(dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.XSD))
				.thenReturn(new XSDDataSourceFactory());
		ParseFromFileRequest parseFromFileRequest = new ParseFromFileRequest("dataSourceName");
		parseFromFileRequest.setMasterFilePath(ResourceUtils.getFile(this.getClass().getResource("/Sample.xsd")).getPath());
		ReflectionTestUtils.setField(xsdFileDataSourceDefinitionParser, "fileSaveDir", "rapido-saved-files");
		xsdFileDataSourceDefinitionParser.parse(parseFromFileRequest);

	}

}
