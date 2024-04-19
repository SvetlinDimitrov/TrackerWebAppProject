package org.record.client;

import java.util.List;

import org.record.client.dto.StorageView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import brave.http.HttpResponse;

@FeignClient(name = "storage")
public interface StorageClient {

    @GetMapping(value = "/api/storage/all")
    ResponseEntity<List<StorageView>> getAllStorages(
            @RequestParam String recordId,
            @RequestHeader("X-ViewUser") String viewUser);

    @PostMapping(value = "/api/storage")
    ResponseEntity<HttpResponse> createStorage(
            @RequestParam(required = false) String storageName,
            @RequestParam String recordId,
            @RequestHeader("X-ViewUser") String viewUser);

    @PostMapping(value = "/api/storage/firstCreation")
    ResponseEntity<HttpResponse> createStorageFirstCreation(
            @RequestParam String recordId,
            @RequestHeader("X-ViewUser") String viewUser);

    @DeleteMapping(value = "/api/storage/delete/all")
    ResponseEntity<HttpResponse> deleteAllStoragesByRecordId(
            @RequestParam String recordId,
            @RequestHeader("X-ViewUser") String viewUser);

    @DeleteMapping(value = "/api/storage/delete/{storageId}/record")
    public ResponseEntity<HttpStatus> deleteStorage(
            @PathVariable String storageId,
            @RequestParam String recordId,
            @RequestHeader("X-ViewUser") String viewUser);

}