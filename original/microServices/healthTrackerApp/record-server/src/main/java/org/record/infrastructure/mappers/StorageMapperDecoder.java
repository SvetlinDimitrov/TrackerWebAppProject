package org.record.infrastructure.mappers;

import java.util.stream.Collectors;
import org.example.domain.storage.dto.StorageView;
import org.record.features.storage.entity.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public abstract class StorageMapperDecoder implements StorageMapper {

  private StorageMapper delegate;
  private CustomFoodMapper customFoodMapper;

  @Override
  public StorageView toView(Storage entity) {
    StorageView view = delegate.toView(entity);
    view.setFoods(
        entity.getFoods()
            .values()
            .stream()
            .map(customFoodMapper::toView)
            .collect(Collectors.toList())
    );

    return view;
  }

  @Autowired
  public void setDelegate(@Qualifier("delegate") StorageMapper delegate) {
    this.delegate = delegate;
  }

  @Autowired
  public void setCustomFoodMapper(CustomFoodMapper customFoodMapper) {
    this.customFoodMapper = customFoodMapper;
  }

}
