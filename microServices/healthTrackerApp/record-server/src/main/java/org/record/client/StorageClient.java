package org.record.client;

import java.util.List;

import org.record.client.dto.StorageView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import brave.http.HttpResponse;

@FeignClient(name = "storage")
public interface StorageClient {

    @GetMapping(value = "/api/storage/all", headers = "X-ViewUser")
    ResponseEntity<List<StorageView>> getAllStorages(
            @RequestParam Long recordId);

    @PostMapping(value = "/api/storage", headers = "X-ViewUser")
    ResponseEntity<HttpResponse> createStorage(
            @RequestParam(required = false) String storageName,
            @RequestParam Long recordId);

    @PostMapping(value = "/api/storage/firstCreation", headers = "X-ViewUser")
    ResponseEntity<HttpResponse> createStorageFirstCreation(
            @RequestParam Long recordId);

    @DeleteMapping(value = "/api/storage/delete/all", headers = "X-ViewUser")
    ResponseEntity<HttpResponse> deleteAllStoragesByRecordId(
            @RequestParam Long recordId);
}