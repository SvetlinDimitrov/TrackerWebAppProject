package org.food.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FoodListException  extends Exception{
        private List<String> availableFoodNames;
        private String message;
}
