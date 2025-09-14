package com.proactis.pma.controller;

import com.proactis.pma.dto.StoreRequest;
import com.proactis.pma.dto.StoreResponse;
import com.proactis.pma.model.Store;
import com.proactis.pma.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping("/stores")
    public ResponseEntity<List<StoreResponse>> index() {
        List<StoreResponse> storeResponseList = storeService.get();
        return ResponseEntity.ok(storeResponseList);
    }

    @PostMapping("/store")
    public ResponseEntity<StoreResponse> create(@Valid @RequestBody StoreRequest request) {
        StoreResponse storeResponse = storeService.create(request);
        return ResponseEntity.ok(storeResponse);
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<StoreResponse> show(@PathVariable UUID id) {
        StoreResponse storeResponse = storeService.find(id);
        return ResponseEntity.ok(storeResponse);
    }

    @PatchMapping("/store/{id}")
    public ResponseEntity<StoreResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody StoreRequest request
    ) {
        StoreResponse storeResponse = storeService.update(id, request);
        return ResponseEntity.ok(storeResponse);
    }

    @DeleteMapping("/store/{id}")
    public ResponseEntity<Void> destroy(@PathVariable UUID id) {
        storeService.destroy(id);
        return ResponseEntity.noContent().build();
    }
}
