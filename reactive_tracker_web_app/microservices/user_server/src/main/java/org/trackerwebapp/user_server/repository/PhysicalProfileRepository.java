package org.trackerwebapp.user_server.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.trackerwebapp.user_server.domain.entity.PhysicalProfileDetails;

@Repository
public interface PhysicalProfileRepository extends R2dbcRepository<PhysicalProfileDetails, String> {

}
