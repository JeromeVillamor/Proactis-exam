package com.proactis.pma.service;

import com.proactis.pma.dto.ProductRequest;
import com.proactis.pma.dto.ProductResponse;
import com.proactis.pma.exception.ProductNotFoundException;
import com.proactis.pma.model.Product;
import com.proactis.pma.model.Store;
import com.proactis.pma.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getProductsByStore(String storeName) {
        Store store = storeService.getStore(storeName);
        List<Product> products = store.getProducts();

        return products.stream()
                .map(ProductResponse::fromProduct)
                .toList();
    }

    public ProductResponse create(String storeName, ProductRequest request) {
        Store store = storeService.getStore(storeName);

        Product product = request.toProduct();
        product.setStore(store);
        Product saveProduct = productRepository.save(product);

        return ProductResponse.fromProduct(saveProduct);
    }

    public List<ProductResponse> createBulk(String storeName, List<ProductRequest> requests) {
        Store store = storeService.getStore(storeName);

        List<Product> products = requests.stream()
                .map(r -> {
                    Product product = r.toProduct();
                    product.setStore(store);
                    return product;
                })
                .toList();


        List<Product> saveProducts = productRepository.saveAll(products);

        return saveProducts.stream()
                .map(ProductResponse::fromProduct)
                .toList();
    }

    public ProductResponse find(String storeName, UUID id) {
        Product product = this.getProductById(storeName, id);
        return ProductResponse.fromProduct(product);
    }

    public ProductResponse update(String storeName, UUID id, ProductRequest request) {
        Product product = this.getProductById(storeName, id);
        product.setName(request.getName());
        product.setSku(request.getSku());

        Product saveProduct = productRepository.save(product);
        return ProductResponse.fromProduct(saveProduct);
    }


    public void destroy(String storeName, UUID id) {
        Product product = this.getProductById(storeName, id);
        productRepository.delete(product);
    }

    private Product getProductById(String storeName, UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if(! product.getStore().getName().equals(storeName)) {
            throw new ProductNotFoundException("Product is not under store");
        }

        return product;
    }
}
