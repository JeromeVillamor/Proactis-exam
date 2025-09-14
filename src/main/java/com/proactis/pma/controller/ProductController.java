package com.proactis.pma.controller;

import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.ProductResponse;
import com.proactis.pma.model.Product;
import com.proactis.pma.repository.ProductRepository;
import com.proactis.pma.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/store/{storeId}/products")
    public ResponseEntity<List<ProductResponse>> index(@PathVariable UUID storeId) {
        List<ProductResponse>  productResponseList = productService.getProductsByStore(storeId);
        return ResponseEntity.ok(productResponseList);
    }

    @PostMapping("/store/{storeId}/product")
    public ResponseEntity<ProductResponse> create(
            @PathVariable UUID storeId,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse productResponse = productService.create(storeId, request);
        return ResponseEntity.ok(productResponse);
    }

    @PostMapping("/store/{storeId}/products")
    public ResponseEntity<List<ProductResponse>> createBulk(
            @PathVariable UUID storeId,
            @RequestBody @Valid List<ProductRequest> requests
    ) {
        List<ProductResponse> productResponseList = productService.createBulk(storeId, requests);
        return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/store/{storeId}/product/{id}")
    public ResponseEntity<ProductResponse> show(
            @PathVariable UUID storeId,
            @PathVariable UUID id
    ) {
        ProductResponse productResponse = productService.find(storeId, id);
        return ResponseEntity.ok(productResponse);
    }

    @PatchMapping("/store/{storeId}/product/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID storeId,
            @PathVariable UUID id,
            @RequestBody ProductRequest request
            ) {
        ProductResponse productResponse = productService.update(storeId, id, request);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/store/{storeId}/product/{id}")
    public ResponseEntity<Void> destroy(
            @PathVariable UUID storeId,
            @PathVariable UUID id
    ) {
        productService.destroy(storeId, id);
        return ResponseEntity.noContent().build();
    }
}
