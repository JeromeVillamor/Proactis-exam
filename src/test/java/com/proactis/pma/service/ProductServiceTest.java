package com.proactis.pma.service;

import com.proactis.pma.ObjectFactory;
import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.ProductResponse;
import com.proactis.pma.exception.ProductNotFoundException;
import com.proactis.pma.model.Product;
import com.proactis.pma.model.Store;
import com.proactis.pma.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private StoreService storeService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void it_should_fetch_all_products_under_store() {
        Store store = ObjectFactory.createStore();
        List<Product> products = ObjectFactory.createProducts(store, 5);
        store.setProducts(products);

        when(storeService.getStore(store.getName())).thenReturn(store);

        List<ProductResponse> productResponse = productService.getProductsByStore(store.getName());

        assertEquals(products.size(), productResponse.size());
        for (int i = 0; i < products.size(); i++) {
            assertEquals(products.get(i).getId(), productResponse.get(i).getId());
            assertEquals(products.get(i).getSku(), productResponse.get(i).getSku());
            assertEquals(products.get(i).getName(), productResponse.get(i).getName());
            assertEquals(products.get(i).getStore().getName(), productResponse.get(i).getStore());

            assertNotNull(productResponse.get(i).getCreatedAt());
            assertNotNull(productResponse.get(i).getUpdatedAt());
        }
    }

    @Test
    public void it_should_create_a_product() {
        Store store = ObjectFactory.createStore();
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("Product SKU");
        productRequest.setName("Product Name");

        when(storeService.getStore(store.getName())).thenReturn(store);

        Product product = productRequest.toProduct();
        product.setStore(store);
        when(productRepository.save(any())).thenReturn(product);

        productService.create(store.getName(), productRequest);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product captorValue = captor.getValue();

        assertEquals(captorValue.getSku(), productRequest.getSku());
        assertEquals(captorValue.getName(), productRequest.getName());
    }

    @Test
    public void it_should_create_multiple_products() {
        Store store = ObjectFactory.createStore();
        List<ProductRequest> productRequests = ObjectFactory.createProductRequests(3);

        when(storeService.getStore(store.getName())).thenReturn(store);

        List<Product> products = productRequests.stream()
                .map(req -> {
                    Product product = req.toProduct();
                    product.setStore(store);
                    return product;
                })
                .toList();
        when(productRepository.saveAll(any())).thenReturn(products);

        List<ProductResponse> responses = productService.createBulk(store.getName(), productRequests);

        ArgumentCaptor<List<Product>> captor = ArgumentCaptor.forClass(List.class);
        verify(productRepository).saveAll(captor.capture());
        List<Product> capturedProducts = captor.getValue();

        assertEquals(productRequests.size(), responses.size());

        for (int i = 0; i < productRequests.size(); i++) {
            assertEquals(productRequests.get(i).getSku(), capturedProducts.get(i).getSku());
            assertEquals(productRequests.get(i).getName(), capturedProducts.get(i).getName());
            assertEquals(store, capturedProducts.get(i).getStore());
        }
    }

    @Test
    public void when_getting_the_product_it_should_throw_an_error_if_product_is_not_under_the_store() {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductNotFoundException exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.find("NOT EXISTING STORE", product.getId());
        });

        assertEquals("Product is not under store", exception.getMessage());
    }

    @Test
    public void it_should_show_a_product_under_a_store() {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.find(store.getName(), product.getId());

        assertEquals(product.getId(), productResponse.getId());
        assertEquals(product.getSku(), productResponse.getSku());
        assertEquals(product.getName(), productResponse.getName());
        assertEquals(product.getStore().getName(), productResponse.getStore());

        assertNotNull(productResponse.getCreatedAt());
        assertNotNull(productResponse.getUpdatedAt());
    }

    @Test
    public void when_updating_the_product_it_should_throw_an_error_if_product_is_not_under_the_store() {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductNotFoundException exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.update("NOT EXISTING STORE", product.getId(), new ProductRequest());
        });

        assertEquals("Product is not under store", exception.getMessage());
    }

    @Test
    public void it_should_update_a_product() {
        Store store = ObjectFactory.createStore();
        Product existingProduct = ObjectFactory.createProduct(store);

        when(productRepository.findById(existingProduct.getId())).thenReturn(Optional.of(existingProduct));


        ProductRequest productRequest = new ProductRequest();
        productRequest.setSku("Product SKU - Updated");
        productRequest.setName("Product Name - Updated");

        Product product = productRequest.toProduct();
        product.setStore(store);
        when(productRepository.save(any())).thenReturn(product);

        productService.update(store.getName(), existingProduct.getId(), productRequest);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product captorValue = captor.getValue();

        assertEquals(captorValue.getSku(), productRequest.getSku());
        assertEquals(captorValue.getName(), productRequest.getName());
    }

    @Test
    public void when_deleting_the_product_it_should_throw_an_error_if_product_is_not_under_the_store() {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductNotFoundException exception = Assertions.assertThrows(ProductNotFoundException.class, () -> {
            productService.destroy("NOT EXISTING STORE", product.getId());
        });

        assertEquals("Product is not under store", exception.getMessage());
    }

    @Test
    public void it_should_delete_a_product() {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        productService.destroy(store.getName(), product.getId());

        verify(productRepository).delete(product);
    }

}
