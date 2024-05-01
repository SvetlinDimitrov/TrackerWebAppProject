package org.nutriGuideBuddy.utils;

import java.math.BigDecimal;

public class Validator {

  public static Boolean validateString(String word , Integer lengthInclude , Integer lengthExclude){
    return word != null && !word.isBlank() && word.trim().length() >= lengthInclude && word.trim().length() <= lengthExclude;
  }
  public static Boolean validateBigDecimal(BigDecimal value , BigDecimal minInclude){
    return value != null && value.compareTo(minInclude) >= 0;
  }
}
