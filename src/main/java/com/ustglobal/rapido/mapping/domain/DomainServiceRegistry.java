package com.ustglobal.rapido.mapping.domain;

import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntityCreationService;
import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntityUpdationService;
import com.ustglobal.rapido.mapping.domain.model.businessentity.datasource.db.DatabaseQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceRegistry {

	@Autowired
	private DatabaseQueryService databaseQueryService;

	@Autowired
	private BusinessEntityUpdationService entityUpdationService;

	@Autowired
	private BusinessEntityCreationService entityCreationService;

	public BusinessEntityCreationService getEntityCreationService() {
		return entityCreationService;
	}

	public BusinessEntityUpdationService getEntityUpdationService() {
		return entityUpdationService;
	}

	public DatabaseQueryService getDatabaseQueryService() {
		return databaseQueryService;
	}
}
