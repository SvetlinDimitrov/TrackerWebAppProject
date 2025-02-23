package org.record.features.record.services;

import static org.record.infrastructure.exception.ExceptionMessages.RECORD_ALREADY_EXISTS;
import static org.record.infrastructure.exception.ExceptionMessages.RECORD_NOT_FOUND;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.domain.user.dto.UserView;
import org.example.exceptions.throwable.NotFoundException;
import org.example.util.UserExtractor;
import org.record.features.record.dto.RecordCreateRequest;
import org.record.features.record.dto.RecordUpdateReqeust;
import org.record.features.record.dto.RecordView;
import org.record.features.record.entity.Record;
import org.record.features.record.repository.RecordRepository;
import org.record.features.record.repository.RecordSpecification;
import org.record.features.record.utils.RecordUtils;
import org.record.infrastructure.mappers.RecordMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordServiceImp implements RecordService {

  private final RecordMapper recordMapper;
  private final RecordRepository repository;

  public Page<RecordView> getAll(String userToken, Pageable pageable) {
    UserView user = UserExtractor.get(userToken);

    return repository.findAll(new RecordSpecification(user.id()), pageable)
        .map(record -> recordMapper.toView(record, user));
  }

  public RecordView getById(UUID recordId, String userToken) {
    var user = UserExtractor.get(userToken);

    Record record = findByIdAndUserId(recordId, user.id());

    return recordMapper.toView(record, user);
  }

  public RecordView create(RecordCreateRequest dto , String userToken) {
    UserView user = UserExtractor.get(userToken);

    if (repository.existsByDateAndUserId(dto.date(), user.id())) {
      return recordMapper.toView(repository.findByDateAndUserId(dto.date(), user.id()), user);
    }

    var record = recordMapper.toEntity(dto, user);

    return recordMapper.toView(repository.save(record), user);
  }

  @Transactional
  public void delete(UUID recordId, String userToken) {
    UUID userId = UserExtractor.get(userToken).id();

    if (!repository.existsByIdAndUserId(recordId, userId)) {
      throw new NotFoundException(RECORD_NOT_FOUND , recordId);
    }

    repository.deleteByIdAndUserId(recordId, userId);
  }

  public Record findByIdAndUserId(UUID recordId, UUID userId) {
    return repository.findByIdAndUserId(recordId, userId)
        .orElseThrow(() -> new NotFoundException(RECORD_NOT_FOUND , recordId));
  }

  public RecordView update(UUID id, String userToken, RecordUpdateReqeust dto) {
    var user = UserExtractor.get(userToken);
    var record = findByIdAndUserId(id, user.id());

    recordMapper.update(record, dto);

    return recordMapper.toView(repository.save(record), user);
  }
}
