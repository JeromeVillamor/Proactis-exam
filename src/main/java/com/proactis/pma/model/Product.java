package com.proactis.pma.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product extends BaseModel {

    @Id
    private UUID id;

    @Column(nullable = false, length = 30, unique = true)
    private String sku;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @PrePersist
    public void idGenerator() {
        if(this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
