package org.record.features.record.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.record.features.storage.entity.Storage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "records")
public class Record implements Serializable {

  @Id
  private String id;
  private String name;
  private UUID userId;
  private LocalDateTime date;
  private List<Storage> storage;
  private BigDecimal dailyCalories = BigDecimal.ZERO;
}
