package org.nutriGuideBuddy.domain.dto.record;


import org.nutriGuideBuddy.domain.enums.Goals;

import java.util.List;

public record CreateRecord(Goals goal, DistributedMacros distributedMacros, List<NutritionView> nutritions) {

}
