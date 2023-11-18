package org.record.client;

import java.util.List;

import org.record.client.dto.StorageView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "storage")
public interface StorageClient {

    @GetMapping(path = "/api/storage")
    List<StorageView> getAllStorageWithRecordId(@RequestParam("recordId") Long recordId,
            @RequestHeader("X-ViewUser") String userToken);

    @PostMapping("/api/storage")
    ResponseEntity<List<StorageView>> firstCreationOfRecord(@RequestParam("recordId") Long recordId , @RequestHeader("X-ViewUser") String userToken);
}
