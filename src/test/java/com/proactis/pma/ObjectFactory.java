package com.proactis.pma;

import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.StoreRequest;
import com.proactis.pma.model.Product;
import com.proactis.pma.model.Store;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class ObjectFactory {

    private static final Faker faker = new Faker();

    public static StoreRequest storeRequest() {
        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("Store Name");
        storeRequest.setLocation("Store Location");
        return storeRequest;
    }

    public static Store createStore() {
        Store store = new Store();
        store.setId(UUID.randomUUID());
        store.setName(faker.company().name());
        store.setLocation(faker.address().fullAddress());
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());
        return store;
    }

    public static List<Store> createStores(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createStore())
                .toList();
    }

    public static Product createProduct() {
        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setSku(faker.commerce().promotionCode());
        product.setName(faker.commerce().productName());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    public static Product createProduct(Store store) {
        Product product = createProduct();
        product.setStore(store);
        return product;
    }

    public static Product createProduct(Store store, String sku) {
        Product product = createProduct(store);
        product.setSku(sku);
        return product;
    }

    public static List<Product> createProducts(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createProduct())
                .toList();
    }

    public static List<Product> createProducts(Store store, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createProduct(store))
                .toList();
    }

    public static ProductRequest createProductRequest() {
        ProductRequest request = new ProductRequest();
        request.setSku(faker.commerce().promotionCode());
        request.setName(faker.commerce().productName());
        return request;
    }

    public static List<ProductRequest> createProductRequests(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> createProductRequest())
                .toList();
    }

}
