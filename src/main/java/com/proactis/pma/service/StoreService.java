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

        long numberOfProducts = storeRepository.findStoreProductCountsById(store.getId());
        return StoreResponse.fromStore(store, numberOfProducts);
    }

    public StoreResponse find(UUID id) {
        Store store = this.getStore(id);
        long numberOfProducts = storeRepository.findStoreProductCountsById(id);
        return StoreResponse.fromStore(store, numberOfProducts);
    }

    public StoreResponse update(UUID id, StoreRequest request) {
        Store store = this.getStore(id);
        store.setName(request.getName());
        store.setLocation(request.getLocation());

        Store savedStore = storeRepository.save(store);
        long numberOfProducts = storeRepository.findStoreProductCountsById(id);
        return StoreResponse.fromStore(savedStore, numberOfProducts);
    }

    public void destroy(UUID id) {
        long numberOfProducts = storeRepository.findStoreProductCountsById(id);

        if(numberOfProducts > 0) {
            throw new StoreHasProductsException();
        }

        storeRepository.deleteById(id);
    }

    public Store getStore(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(StoreNotFoundException::new);
    }
}
