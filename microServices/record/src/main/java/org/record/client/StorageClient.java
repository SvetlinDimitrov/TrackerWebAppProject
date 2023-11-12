package org.record.client;

import java.util.List;

import org.record.client.dto.StorageView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "${clients.storage.name}")
public interface StorageClient {

    @GetMapping(path = "${clients.storage.retrieve-data.url}")
    List<StorageView> getAllStorageWithRecordId(@RequestParam("recordId") Long recordId,
            @RequestHeader("X-ViewUser") String userToken);

}
