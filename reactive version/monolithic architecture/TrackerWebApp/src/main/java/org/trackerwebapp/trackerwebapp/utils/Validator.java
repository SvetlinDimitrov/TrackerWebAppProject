package org.trackerwebapp.trackerwebapp.utils;

import java.math.BigDecimal;

public class Validator {

  public static Boolean validateString(String word , Integer lengthInclude){
    return word != null && !word.isBlank() && word.trim().length() >= lengthInclude;
  }
  public static Boolean validateBigDecimal(BigDecimal value , BigDecimal minInclude){
    return value != null && value.compareTo(minInclude) >= 0;
  }
}
