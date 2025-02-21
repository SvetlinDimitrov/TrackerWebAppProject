package org.record.features.record.services;

import java.util.List;
import org.example.domain.record.dtos.RecordView;

public interface RecordService {

  List<RecordView> getAll(String userToken);

  RecordView getById(String recordId, String userToken);

  void create(String userToken, String name);

  void delete(String recordId, String userToken);
}
