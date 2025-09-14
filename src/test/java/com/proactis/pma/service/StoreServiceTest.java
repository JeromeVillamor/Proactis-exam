package com.proactis.pma.service;

import com.proactis.pma.ObjectFactory;
import com.proactis.pma.dto.StoreRequest;
import com.proactis.pma.dto.StoreResponse;
import com.proactis.pma.exception.StoreHasProductsException;
import com.proactis.pma.model.Store;
import com.proactis.pma.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    public void it_should_return_all_stores() {
        List<Store> stores = ObjectFactory.createStores(5);
        List<Object[]> productCounts = new ArrayList<>();
        stores.forEach(store -> {
            productCounts.add(new Object[]{store.getId(), 5L});

        });

        when(storeRepository.findAll()).thenReturn(stores);
        when(storeRepository.findStoreProductCounts()).thenReturn(productCounts);


        List<StoreResponse> storeResponseList = storeService.get();

        // Assertions
        assertEquals(stores.size(), storeResponseList.size());
        for (int i = 0; i < stores.size(); i++) {
            assertEquals(stores.get(i).getName(), storeResponseList.get(i).getName());
            assertEquals(stores.get(i).getLocation(), storeResponseList.get(i).getLocation());
            assertEquals(5L, storeResponseList.get(i).getNumberOfProducts());
            assertNotNull(storeResponseList.get(i).getCreatedAt());
            assertNotNull(storeResponseList.get(i).getUpdatedAt());
        }
    }

    @Test
    public void it_should_create_store() {
        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("Mcdonalds");
        storeRequest.setLocation("Manila, Philippines");

        when(storeRepository.save(any())).thenReturn(storeRequest.toStore());
        when(storeRepository.findStoreProductCountsByName(storeRequest.getName())).thenReturn(0l);

        storeService.create(storeRequest);

        // Capture the Store entity passed to save and assert its fields
        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);
        verify(storeRepository).save(captor.capture());
        Store saved = captor.getValue();

        assertEquals("Mcdonalds", saved.getName());
        assertEquals("Manila, Philippines", saved.getLocation());
    }


    @Test
    public void it_should_show_a_store() {
        Store store = ObjectFactory.createStore();

        when(storeRepository.findByName(store.getName())).thenReturn(Optional.of(store));
        when(storeRepository.findStoreProductCountsByName(store.getName())).thenReturn(0l);

        StoreResponse storeResponse = storeService.find(store.getName());

        assertEquals(store.getName(), storeResponse.getName());
        assertEquals(store.getLocation(), storeResponse.getLocation());
        assertEquals(0L, storeResponse.getNumberOfProducts());
        assertNotNull(storeResponse.getCreatedAt());
        assertNotNull(storeResponse.getUpdatedAt());
    }

    @Test
    public void it_should_update_a_store() {
        Store existingStore = ObjectFactory.createStore();
        when(storeRepository.findByName(existingStore.getName())).thenReturn(Optional.of(existingStore));

        StoreRequest storeRequest = new StoreRequest();
        storeRequest.setName("Jolibee");
        storeRequest.setLocation("Manila, Philippines");

        when(storeRepository.save(any())).thenReturn(storeRequest.toStore());
        when(storeRepository.findStoreProductCountsByName(storeRequest.getName())).thenReturn(0l);

        storeService.update(existingStore.getName(), storeRequest);

        // Capture the Store entity passed to save and assert its fields
        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);
        verify(storeRepository).save(captor.capture());
        Store saved = captor.getValue();

        assertEquals(existingStore.getId(), saved.getId());
        assertEquals("Jolibee", saved.getName());
        assertEquals("Manila, Philippines", saved.getLocation());
    }

    @Test
    public void it_should_not_delete_a_store_if_there_are_still_products_attached() {
        Store store = ObjectFactory.createStore();

        when(storeRepository.findStoreProductCountsByName(store.getName())).thenReturn(3l);

        Assertions.assertThrows(StoreHasProductsException.class, () -> {
            storeService.destroy(store.getName());
        });
    }

    @Test
    public void it_should_successfuly_delete_a_store_if_there_are_no_products_attached() {
        Store store = ObjectFactory.createStore();

        when(storeRepository.findByName(store.getName())).thenReturn(Optional.of(store));
        doNothing().when(storeRepository).delete(store);
        when(storeRepository.findStoreProductCountsByName(store.getName())).thenReturn(0l);

        storeService.destroy(store.getName());
    }
}
