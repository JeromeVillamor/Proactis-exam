package com.proactis.pma.controller;

import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.ProductResponse;
import com.proactis.pma.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/store/{storeName}/products")
    public ResponseEntity<List<ProductResponse>> index(@PathVariable String storeName) {
        List<ProductResponse>  productResponseList = productService.getProductsByStore(storeName);
        return ResponseEntity.ok(productResponseList);
    }

    @PostMapping("/store/{storeName}/product")
    public ResponseEntity<ProductResponse> create(
            @PathVariable String storeName,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse productResponse = productService.create(storeName, request);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/store/{storeName}/products")
    public ResponseEntity<List<ProductResponse>> createBulk(
            @PathVariable String storeName,
            @RequestBody @Valid List<ProductRequest> requests
    ) {
        List<ProductResponse> productResponseList = productService.createBulk(storeName, requests);
        return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/store/{storeName}/product/{id}")
    public ResponseEntity<ProductResponse> show(
            @PathVariable String storeName,
            @PathVariable UUID id
    ) {
        ProductResponse productResponse = productService.find(storeName, id);
        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/store/{storeName}/product/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable String storeName,
            @PathVariable UUID id,
            @RequestBody ProductRequest request
            ) {
        ProductResponse productResponse = productService.update(storeName, id, request);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/store/{storeName}/product/{id}")
    public ResponseEntity<Void> destroy(
            @PathVariable String storeName,
            @PathVariable UUID id
    ) {
        productService.destroy(storeName, id);
        return ResponseEntity.noContent().build();
    }
}
