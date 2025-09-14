package com.proactis.pma.dto;

import com.proactis.pma.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "SKU must be alphanumeric only")
    private String sku;

    @NotBlank
    private String name;

    public Product toProduct() {
        Product product = new Product();
        product.setSku(sku);
        product.setName(name);

        return product;
    }
}
