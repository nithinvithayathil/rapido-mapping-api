package com.ustglobal.rapido.mapping.domain.model.businessentity;

import com.ustglobal.rapido.mapping.domain.shared.DomainErrorCode;
import com.ustglobal.rapido.mapping.domain.shared.DomainException;
import com.ustglobal.rapido.mapping.domain.shared.DomainValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BusinessEntityCreationService {

  @Autowired
  private BusinessEntityRepository businessEntityRepository;

  public BusinessEntity newBusinessEntity(String id, String name, String description)
      throws DomainException {
    DomainValidator.notEmpty(id, DomainErrorCode.PARAMETER_REQUIRED, "EntityId");
    DomainValidator.notEmpty(name, DomainErrorCode.PARAMETER_REQUIRED, "Name");
    BusinessEntity businessEntity = new BusinessEntity(
        newBusinessEntityId(id),
        name,
        description);
    businessEntity.addTag(BusinessEntityTag.LATEST);
    return businessEntityRepository.save(businessEntity);
  }

  private BusinessEntityId newBusinessEntityId(String id, String version) throws DomainException {
    return new BusinessEntityId(id, StringUtils.isEmpty(version)? "0.0.1": version);
  }

  private BusinessEntityId newBusinessEntityId(String id) throws DomainException {
    return newBusinessEntityId(id, null);
  }

}
