package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * An application service to update name and description of the business Entity
 */
@Service
public class BusinessEntityUpdationService {

  @Autowired
  private BusinessEntityRepository repository;

  public BusinessEntity updateBusinessEntity(String entityId, String version,
      String newName, String newDescription){

    Optional<BusinessEntity> entityToUpdate = repository.findByBusinessEntityId(
        new BusinessEntityId(entityId, version));

    if(!entityToUpdate.isPresent()){
      throw new DomainException(DomainErrorCode.BUSINESSENTITY_DOES_NOT_EXIST, null, entityId, version);
    }

    entityToUpdate.get().changeName(newName);
    entityToUpdate.get().changeDescription(newDescription);

    if(entityToUpdate.get().hasNonPersistedChanges()){
      return repository.save(entityToUpdate.get());
    }

    return entityToUpdate.get();
  }

}
