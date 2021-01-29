package com.ustglobal.rapido.mapping.infrastructure.web.mappers;

import com.ustglobal.rapido.mapping.domain.model.businessentity.BusinessEntity;
import com.ustglobal.rapido.mapping.infrastructure.web.BusinessEntityPreview;
import java.util.Collection;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Component
public class BusinessEntityPreviewMapper extends Mapper<BusinessEntityPreview, BusinessEntity> {

  @Override
  public BusinessEntityPreview mapFrom(BusinessEntity entity) {
    if(ObjectUtils.isEmpty(entity)){
      return null;
    }
    return new BusinessEntityPreview.Builder()
        .id(entity.getId().getEntityId())
        .version(entity.getId().getVersion().toString())
        .name(entity.getName())
        .description(entity.getDescription())
        .isLocked(entity.isLocked())
        .isBaseline(entity.isBaseline())
        .isLatest(entity.isLatest())
        .hasTarget(entity.getTarget() != null)
        .sourceCount(getCountOf(entity.getSources()))
        .mappingsCount(getCountOf(entity.getMappings()))
        .build();

  }

  private int getCountOf(Collection collection) {
    int count = 0;
    if(CollectionUtils.isEmpty(collection)){
      return count;
    }
    return collection.size();
  }

}
