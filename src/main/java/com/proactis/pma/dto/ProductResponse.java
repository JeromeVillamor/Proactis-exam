package com.proactis.pma.dto;

import com.proactis.pma.model.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductResponse {

    private UUID id;
    private String sku;
    private String name;
    private String store;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setStore(product.getStore().getName());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

}
