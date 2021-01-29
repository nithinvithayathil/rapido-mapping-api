package com.ustglobal.rapido.mapping.application;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.*;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.parserequest.*;

/**
 * Application service to import elements from XSD file as a datasource
 */
@Service
public class ImportDataSourceFromXSDService {

	@Autowired
	private ApplicationServiceRegistry applicationServiceRegistry;

	@Autowired
	private DataSourceFactoryProvider dataSourceFactoryProvider;

	@Autowired
	private ParseRequestFactory parseRequestFactory;

	@Value("${rapido.file.save.dir}")
	private String fileSaveDir;

	/**
	 * Imports all elements from the specified xsd files in to a single datasource.
	 * 
	 * @param masterXSDName
	 *            name of master xsd file for the list of multipart files
	 * @param multipartFiles
	 *            have list of master xsd file and reference xsd's
	 * @return
	 */
	public DataSourceDefinition importDatasourceFromXSDFiles(String masterXSDName, MultipartFile[] multipartFiles, String dataSourceName) {
		DomainValidator.notEmpty(multipartFiles, DomainErrorCode.PARAMETER_REQUIRED, "MultipartFiles");
    DomainValidator.notEmpty(masterXSDName, DomainErrorCode.PARAMETER_REQUIRED, "MasterXSDName");

		// Persist files to local fs
		applicationServiceRegistry.getFilePersistService().save(multipartFiles);

		DataSourceFactory dataSourceFactory = getDataSourceFactory();
		DataSourceDefinitionParser dataSourceDefinitionParser = getDataSourceDefinitionParser(dataSourceFactory);

		ParseFromFileRequest fromXSDFileRequest = createParseFromFileRequest(dataSourceName);

		fromXSDFileRequest.setMasterFilePath(getAbsolutePathOfMasterFile(masterXSDName));

		return dataSourceDefinitionParser.parse(fromXSDFileRequest);
	}

	private ParseFromFileRequest createParseFromFileRequest(String dataSourceName) {
		return (ParseFromFileRequest) parseRequestFactory.createParseRequest(ImportSourceType.FILE, dataSourceName);
	}

	private DataSourceDefinitionParser getDataSourceDefinitionParser(DataSourceFactory dataSourceFactory) {
		return dataSourceFactory.createDataSourceDefinitionParser(ImportSourceType.FILE);
	}

	private DataSourceFactory getDataSourceFactory() {
		return dataSourceFactoryProvider.getDataSourceFactory(DataSourceDefinitionType.XSD);
	}

	private String getAbsolutePathOfMasterFile(String masterXSDName) {
		Path masterFilePath = Paths.get(String.join(File.separator, fileSaveDir, masterXSDName));
		Validate.notNull(masterFilePath, "Master File Path not available.");
		return masterFilePath.toAbsolutePath().toString();
	}

}
