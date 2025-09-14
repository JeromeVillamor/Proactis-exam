package com.proactis.pma.repository;

import com.proactis.pma.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("SELECT s.id, COUNT(p.id) FROM Store s LEFT JOIN s.products p GROUP BY s.id")
    List<Object[]> findStoreProductCounts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.store.id = :storeId")
    long findStoreProductCountsById(@Param("storeId") UUID storeId);


}
