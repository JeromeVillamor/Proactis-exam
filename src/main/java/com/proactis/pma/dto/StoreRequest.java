package com.proactis.pma.dto;

import com.proactis.pma.model.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StoreRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 255)
    private String location;

    public Store toStore() {
        Store store = new Store();
        store.setName(name);
        store.setLocation(location);

        return store;
    }

}
