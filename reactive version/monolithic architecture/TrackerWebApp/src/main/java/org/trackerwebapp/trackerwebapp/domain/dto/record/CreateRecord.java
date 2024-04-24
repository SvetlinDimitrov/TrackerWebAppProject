package org.trackerwebapp.trackerwebapp.domain.dto.record;


import org.trackerwebapp.trackerwebapp.domain.enums.Goals;

public record CreateRecord(Goals goal, DistributedMacros distributedMacros) {

}
