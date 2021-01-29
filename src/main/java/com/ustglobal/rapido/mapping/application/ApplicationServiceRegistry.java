package com.ustglobal.rapido.mapping.application;

import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntityCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceRegistry {

	@Autowired
	private BusinessEntityCreationService businessEntityCreationService;

	@Autowired
	private ImportDataSourceDefinitionFromDatabaseService importDataSourceDefinitionFromDatabaseService;

	@Autowired
	private ImportDataSourceFromXSDService importDataSourceFromXSDService;

	@Autowired
	private FilePersistService filePersistService;

	public BusinessEntityCreationService getBusinessEntityCreationService() {
		return businessEntityCreationService;
	}

	public ImportDataSourceDefinitionFromDatabaseService getImportDataSourceDefinitionFromDatabaseService() {
		return importDataSourceDefinitionFromDatabaseService;
	}

	public ImportDataSourceFromXSDService getImportDataSourceFromXSDService() {
		return importDataSourceFromXSDService;
	}

	public FilePersistService getFilePersistService() {
		return filePersistService;
	}

}
