package com.proactis.pma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.proactis.pma.ObjectFactory;
import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.ProductResponse;
import com.proactis.pma.model.Product;
import com.proactis.pma.model.Store;
import com.proactis.pma.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void test_index_method() throws Exception {
        Store store = ObjectFactory.createStore();
        List<ProductResponse> productResponseList = ObjectFactory.createProducts(store, 5)
                                                        .stream()
                                                        .map(ProductResponse::fromProduct)
                                                        .toList();

        when(productService.getProductsByStore(store.getName()))
                .thenReturn(productResponseList);

        String uri = "/store/" + store.getName() + "/products";
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sku").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].store").exists())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].updatedAt").exists());
    }

    @Test
    public void test_create_method() throws Exception {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);
        ProductRequest productRequest = ObjectFactory.createProductRequest();

        when(productService.create(store.getName(), productRequest))
                .thenReturn(ProductResponse.fromProduct(product));

        String uri = "/store/" + store.getName() + "/product";
        mockMvc.perform(
                post(uri)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(productRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sku").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.store").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void test_bulk_create_method() throws Exception {
        Store store = ObjectFactory.createStore();
        List<ProductRequest> productRequests = ObjectFactory.createProductRequests(3);
        List<ProductResponse> productResponses = ObjectFactory.createProducts(store, 3)
                                                    .stream()
                                                    .map(ProductResponse::fromProduct)
                                                    .toList();

        when(productService.createBulk(store.getName(), productRequests))
                .thenReturn(productResponses);

        String uri = "/store/" + store.getName() + "/products";
        mockMvc.perform(
                post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(productRequests))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sku").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].store").exists())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].updatedAt").exists());
    }

    @Test
    public void test_show_method() throws Exception {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        when(productService.find(store.getName(), product.getId()))
                .thenReturn(ProductResponse.fromProduct(product));

        String uri = "/store/" + store.getName() + "/product/" + product.getId();
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sku").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.store").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void test_update_method() throws Exception {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);
        ProductRequest productRequest = ObjectFactory.createProductRequest();

        when(productService.update(store.getName(), product.getId(), productRequest))
                .thenReturn(ProductResponse.fromProduct(product));

        String uri = "/store/" + store.getName() + "/product/" + product.getId();
        mockMvc.perform(
                patch(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(productRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sku").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.store").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    public void test_delete_method() throws Exception {
        Store store = ObjectFactory.createStore();
        Product product = ObjectFactory.createProduct(store);

        doNothing().when(productService).destroy(store.getName(), product.getId());

        String uri = "/store/" + store.getName() + "/product/" + product.getId();
        mockMvc.perform(delete(uri))
                .andExpect(status().is(204));
    }
}
