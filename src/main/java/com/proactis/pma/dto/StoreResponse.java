package com.proactis.pma.dto;

import com.proactis.pma.model.Store;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreResponse {

    private UUID id;
    private String name;
    private String location;
    private long numberOfProducts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StoreResponse fromStore(Store store, long numberOfProducts) {
        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setId(store.getId());
        storeResponse.setName(store.getName());
        storeResponse.setLocation(store.getLocation());
        storeResponse.setNumberOfProducts(numberOfProducts);
        storeResponse.setCreatedAt(store.getCreatedAt());
        storeResponse.setUpdatedAt(store.getUpdatedAt());
        return storeResponse;
    }
}
