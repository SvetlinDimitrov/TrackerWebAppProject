package org.storage.model.errorResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodErrorResponse {

    private String errorMessage;
    private List<String> availableFoods;
}
