package org.record.infrastructure.mappers;

import org.mapstruct.Mapping;
import org.record.features.record.dto.RecordUpdateReqeust;
import org.record.features.record.dto.RecordView;
import org.example.domain.user.dto.UserView;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
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

  @Mapping(target = "id", source = "entity.id")
  RecordView toView(Record entity  , UserView user);

  void update(@MappingTarget Record entity, RecordUpdateReqeust dto);
}
