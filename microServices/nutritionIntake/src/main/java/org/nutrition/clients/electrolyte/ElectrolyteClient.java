package org.nutrition.clients.electrolyte;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "${clients.electrolyte.name}",
        url = "${clients.electrolyte.domainUrl}")
public interface ElectrolyteClient {

    @GetMapping(path = "${clients.electrolyte.retrieve-data.url}")
    List<ElectrolyteView> getAllElectrolytes();
}
