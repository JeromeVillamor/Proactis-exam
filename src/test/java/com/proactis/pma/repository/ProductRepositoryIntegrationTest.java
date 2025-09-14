package com.proactis.pma.repository;

import com.proactis.pma.ObjectFactory;
import com.proactis.pma.model.Product;
import com.proactis.pma.model.Store;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void should_allow_same_sku_in_different_stores() {
        Store store1 = ObjectFactory.createStore();
        Store store2 = ObjectFactory.createStore();

        store1 = entityManager.persistAndFlush(store1);
        store2 = entityManager.persistAndFlush(store2);

        Product product1 = ObjectFactory.createProduct(store1, "SKU123");
        Product product2 = ObjectFactory.createProduct(store2, "SKU123");

        entityManager.persistAndFlush(product1);
        entityManager.persistAndFlush(product2);

        assertEquals(2, productRepository.count());
    }

    @Test
    public void should_throw_exception_for_duplicate_sku_in_same_store() {
        Store store = ObjectFactory.createStore();
        store = entityManager.persistAndFlush(store);

        Product product1 = ObjectFactory.createProduct(store, "DUPLICATE");
        entityManager.persistAndFlush(product1);

        Product product2 = ObjectFactory.createProduct(store, "DUPLICATE");

        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(product2);
        });
    }
}