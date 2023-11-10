package org.record.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
@FeignClient(name = "${clients.storage.name}")
public interface StorageClient {

    @GetMapping(path = "${clients.storage.retrieve-data.url}")
    List<StorageView> getAllStorageWithRecordId(@PathVariable("recordId") Long recordId);

}
