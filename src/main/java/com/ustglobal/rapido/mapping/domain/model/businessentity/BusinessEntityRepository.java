package com.ustglobal.rapido.mapping.domain.model.businessentity;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface BusinessEntityRepository {

	/**
	 * This method gives the BusinessEntity details if the businessEntityId was
	 * persisted in DB
	 * 
	 * @param businessEntityId
	 * @return
	 */
	Optional<BusinessEntity> findByBusinessEntityId(BusinessEntityId businessEntityId);

	/**
	 * Persists business entity
	 * 
	 * @param businessEntity
	 */
	BusinessEntity save(BusinessEntity businessEntity);

	/**
	 * Get all business entity in the repository
	 * 
	 * @return List of BusinessEntity
	 */
	List<BusinessEntity> findAll();

	/**
	 * Get all business entities latest version in the repostiory
	 * @return List of BusinessEntity
	 */
	List<BusinessEntity> findAllLatestVersions();

	/**
	 * Get business entities that owner the example provided
	 * 
	 * @param id
	 *            Id of the business entity to be retrieved
	 * @return List of BusinessEntity
	 */
	List<BusinessEntity> findBusinessEntitiesWithId(String id);

	/**
	 * Delete a business entity from repository
	 * 
	 * @param id
	 *            Business entity id to be deleted
	 */
	void deleteById(BusinessEntityId id);

}
