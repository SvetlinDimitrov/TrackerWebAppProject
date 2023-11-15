package org.nutrition.clients.electrolyte;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "electrolyte")
public interface ElectrolyteClient {

    @GetMapping(path = "/api/electrolyte")
    List<ElectrolyteView> getAllElectrolytes();
}
