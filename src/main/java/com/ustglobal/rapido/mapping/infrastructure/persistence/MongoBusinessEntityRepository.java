package com.ustglobal.rapido.mapping.infrastructure.persistence;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntity;
import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntityId;
import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntityRepository;

/***
 * This repository implementation helps to persist the BusinessEntity
 *
 */
@Repository
public interface MongoBusinessEntityRepository
		extends MongoRepository<BusinessEntity, BusinessEntityId>, BusinessEntityRepository {
	@Query("{ 'businessEntityId.entityId' : ?0 }")
	List<BusinessEntity> findBusinessEntitiesWithId(String id);

	@Query("{tags:{$all:[\"LATEST\"]}}")
	List<BusinessEntity> findAllLatestVersions();
}
