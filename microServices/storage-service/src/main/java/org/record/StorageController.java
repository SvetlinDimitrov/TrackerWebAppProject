package org.record;

import lombok.RequiredArgsConstructor;
import org.record.model.dto.StorageView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/storage")
public class StorageController {

    private final StorageService storageService;

    @GetMapping("/{recordId}")
    public ResponseEntity<List<StorageView>> getAllStorages(@PathVariable Long recordId){
        List<StorageView> allByRecordId = storageService.getAllByRecordId(recordId);
        return new ResponseEntity<>(allByRecordId , HttpStatus.OK);
    }


}
