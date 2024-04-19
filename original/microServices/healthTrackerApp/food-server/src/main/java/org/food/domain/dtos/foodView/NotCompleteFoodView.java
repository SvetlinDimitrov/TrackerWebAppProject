package org.food.domain.dtos.foodView;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotCompleteFoodView {
        private String id;
        private String description;
        private String foodClass;
}
