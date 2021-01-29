package com.ustglobal.rapido.mapping.domain.model.businessentity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityVersionGroupRepository {

  @Autowired
  protected BusinessEntityRepository repository;

  public EntityVersionGroup findById(String entityId) {
    List<BusinessEntity> entities = repository.findBusinessEntitiesWithId(entityId);
    return new EntityVersionGroup(entityId, entities);
  }

  public EntityVersionGroup save(EntityVersionGroup entityVersionGroup) {
    entityVersionGroup.save(repository);
    return findById(entityVersionGroup.getEntityId());
  }
}
