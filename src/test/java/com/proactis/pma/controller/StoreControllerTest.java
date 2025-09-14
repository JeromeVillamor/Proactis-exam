package com.proactis.pma.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.proactis.pma.ObjectFactory;
import com.proactis.pma.dto.StoreRequest;
import com.proactis.pma.dto.StoreResponse;
import com.proactis.pma.model.Store;
import com.proactis.pma.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerTest {

    @MockitoBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Test
    public void test_index_endpoint() throws Exception {
        List<StoreResponse> storeResponseList = ObjectFactory.createStores(5)
                                                    .stream()
                                                    .map(s -> StoreResponse.fromStore(s, 0))
                                                    .toList();

        when(storeService.get()).thenReturn(storeResponseList);

        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].location").exists())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].updatedAt").exists())
                .andExpect(jsonPath("$[0].numberOfProducts").value(0));
    }

    @Test
    public void test_create_method() throws Exception {
        Store store = ObjectFactory.createStore();
        StoreRequest storeRequest = ObjectFactory.storeRequest();


        when(storeService.create(storeRequest))
                .thenReturn(StoreResponse.fromStore(store, 0));

        mockMvc.perform(
                post("/store")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(writer.writeValueAsString(storeRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.numberOfProducts").value(0));
    }

    @Test
    public void test_show_method() throws Exception {
        Store store = ObjectFactory.createStore();

        when(storeService.find(store.getName()))
                .thenReturn(StoreResponse.fromStore(store, 0));

        mockMvc.perform(get("/store/" + store.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.numberOfProducts").value(0));
    }

    @Test
    public void test_update_method() throws Exception {
        Store store = ObjectFactory.createStore();
        StoreRequest storeRequest = ObjectFactory.storeRequest();


        when(storeService.update(store.getName(), storeRequest))
                .thenReturn(StoreResponse.fromStore(store, 0));

        mockMvc.perform(
                        patch("/store/" + store.getName())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(writer.writeValueAsString(storeRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.location").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists())
                .andExpect(jsonPath("$.numberOfProducts").value(0));
    }

    @Test
    public void test_delete_method() throws Exception {
        Store store = ObjectFactory.createStore();

        doNothing().when(storeService).destroy(store.getName());

        mockMvc.perform(delete("/store/" + store.getName()))
                .andExpect(status().is(204));
    }
}
