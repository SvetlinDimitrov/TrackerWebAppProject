package org.nutrition.clients.macronutrient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "macronutrient")
public interface MacronutrientClient {

    @GetMapping(path = "/api/macronutrient")
    List<MacronutrientView> getAllMacronutrients();
}
