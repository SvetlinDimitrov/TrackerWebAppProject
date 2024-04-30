package org.trackerwebapp.trackerwebapp.domain.dto.meal;

import org.trackerwebapp.trackerwebapp.domain.entity.FoodInfoEntity;

public record FoodInfoView(
    String info,
    String moreInfo,
    String picture
) {

  public static FoodInfoView toView(FoodInfoEntity foodInfoView) {
    return new FoodInfoView(foodInfoView.getInfo() , foodInfoView.getMoreInfo() , foodInfoView.getPicture());
  }
}
