package org.trackerwebapp.trackerwebapp.domain.dto.record;


import org.trackerwebapp.trackerwebapp.domain.enums.Goals;

import java.util.List;

public record CreateRecord(Goals goal, DistributedMacros distributedMacros, List<NutritionView> nutritions) {

}
