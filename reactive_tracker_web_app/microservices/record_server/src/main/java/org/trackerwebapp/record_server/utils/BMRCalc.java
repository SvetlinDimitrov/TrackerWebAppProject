package org.trackerwebapp.record_server.utils;

import java.math.BigDecimal;
import org.trackerwebapp.record_server.domain.entity.UserDetailsEntity;
import org.trackerwebapp.shared_interfaces.domain.enums.Gender;

public class BMRCalc {

  public static BigDecimal calculateBMR(UserDetailsEntity userDetails){
    BigDecimal BMR;

    if (userDetails.getGender().equals(Gender.MALE)) {
      BMR = new BigDecimal("88.362")
          .add(new BigDecimal("13.397").multiply(userDetails.getKilograms()))
          .add(new BigDecimal("4.799").multiply(userDetails.getHeight()))
          .subtract(new BigDecimal("5.677")
              .add(new BigDecimal(userDetails.getAge())));
    } else {
      BMR = new BigDecimal("447.593")
          .add(new BigDecimal("9.247").multiply(userDetails.getKilograms()))
          .add(new BigDecimal("3.098").multiply(userDetails.getHeight()))
          .subtract(new BigDecimal("4.330")
              .add(new BigDecimal(userDetails.getAge())));
    }
    return BMR;
  }
}
