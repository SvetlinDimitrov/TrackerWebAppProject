package org.record.infrastructure.mappers;

import org.example.domain.storage.dto.StorageView;
import org.mapstruct.DecoratedWith;
import org.record.features.storage.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(StorageMapperDecoder.class)
public interface StorageMapper {

  @Mapping(ignore = true , target = "foods")
  StorageView toView(Storage entity);
}
