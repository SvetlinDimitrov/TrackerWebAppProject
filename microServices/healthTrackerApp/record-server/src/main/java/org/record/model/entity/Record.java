package org.record.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "records")
public class Record {

    @Id
    private String id;
    private String name;
    private BigDecimal dailyCalories = BigDecimal.ZERO;
    private String userId;
    private LocalDate date;

}
