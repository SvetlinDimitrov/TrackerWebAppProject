package org.record.infrastructure.mappers;

import org.example.domain.record.dtos.RecordView;
import org.example.domain.user.dto.UserView;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.record.features.record.entity.Record;

@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
@DecoratedWith(RecordMapperDecoder.class)
public interface RecordMapper {

  @Mapping(source = "entity.id", target = "id")
  @Mapping(source = "entity.name", target = "name")
  @Mapping(source = "entity.date", target = "date")
  RecordView toView(Record entity  , UserView user);
}
