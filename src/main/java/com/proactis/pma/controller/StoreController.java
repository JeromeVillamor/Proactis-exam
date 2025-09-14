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

    @GetMapping("/store/{name}")
    public ResponseEntity<StoreResponse> show(@PathVariable String name) {
        StoreResponse storeResponse = storeService.find(name);
        return ResponseEntity.ok(storeResponse);
    }

    @PatchMapping("/store/{name}")
    public ResponseEntity<StoreResponse> update(
            @PathVariable String name,
            @Valid @RequestBody StoreRequest request
    ) {
        StoreResponse storeResponse = storeService.update(name, request);
        return ResponseEntity.ok(storeResponse);
    }

    @DeleteMapping("/store/{name}")
    public ResponseEntity<Void> destroy(@PathVariable String name) {
        storeService.destroy(name);
        return ResponseEntity.noContent().build();
    }
}
