package com.ustglobal.rapido.mapping.infrastructure.web.mappers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.CollectionUtils;

public abstract class Mapper<T, K> {

  public abstract T mapFrom(K obj);

  public List<T> mapListFrom(List<K> objs) {
    List<T> newList = new ArrayList<>();
    if (!CollectionUtils.isEmpty(objs)){
      for (K obj : objs) {
        newList.add(mapFrom(obj));
      }
    }
    return newList;
  }

}
