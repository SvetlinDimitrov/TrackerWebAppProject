package org.nutrition.clients.macronutrient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "${clients.macronutrient.name}",
        url = "${clients.macronutrient.domainUrl}")
public interface MacronutrientClient {

    @GetMapping(path = "${clients.macronutrient.retrieve-data.url}")
    List<MacronutrientView> getAllMacronutrients();
}
