package org.trackerwebapp.record_server.domain.dto;

import org.trackerwebapp.shared_interfaces.domain.enums.Goals;

public record CreateRecord(Goals goal, DistributedMacros distributedMacros) {

}
