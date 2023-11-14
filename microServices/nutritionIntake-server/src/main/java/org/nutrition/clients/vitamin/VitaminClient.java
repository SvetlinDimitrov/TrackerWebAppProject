package org.nutrition.clients.vitamin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Component
@FeignClient(name = "vitamin")
public interface VitaminClient {
    @GetMapping(path = "/api/vitamin")
    List<VitaminDto> getAllVitamins();
}
