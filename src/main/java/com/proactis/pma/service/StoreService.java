package com.proactis.pma.service;

import com.proactis.pma.dto.StoreRequest;
import com.proactis.pma.dto.StoreResponse;
import com.proactis.pma.exception.StoreHasProductsException;
import com.proactis.pma.exception.StoreNotFoundException;
import com.proactis.pma.model.Store;
import com.proactis.pma.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<StoreResponse> get() {
        List<Store> stores = storeRepository.findAll();
        List<Object[]> productCounts = storeRepository.findStoreProductCounts();

        Map<UUID, Long> countMap = productCounts.stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Long) row[1]
                ));

        return stores.stream()
                .map(store -> StoreResponse.fromStore(store, countMap.get(store.getId())))
                .toList();
    }

    public StoreResponse create(StoreRequest request) {
        Store store = storeRepository.save(request.toStore());

        long numberOfProducts = storeRepository.findStoreProductCountsByName(store.getName());
        return StoreResponse.fromStore(store, numberOfProducts);
    }

    public StoreResponse find(String name) {
        Store store = this.getStore(name);
        long numberOfProducts = storeRepository.findStoreProductCountsByName(name);
        return StoreResponse.fromStore(store, numberOfProducts);
    }

    public StoreResponse update(String name, StoreRequest request) {
        Store store = this.getStore(name);
        store.setName(request.getName());
        store.setLocation(request.getLocation());

        Store savedStore = storeRepository.save(store);
        long numberOfProducts = storeRepository.findStoreProductCountsByName(request.getName());
        return StoreResponse.fromStore(savedStore, numberOfProducts);
    }

    public void destroy(String name) {
        long numberOfProducts = storeRepository.findStoreProductCountsByName(name);

        if(numberOfProducts > 0) {
            throw new StoreHasProductsException();
        }

        Store store = this.getStore(name);
        storeRepository.delete(store);
    }

    public Store getStore(String name) {
        return storeRepository.findByName(name)
                .orElseThrow(StoreNotFoundException::new);
    }
}
