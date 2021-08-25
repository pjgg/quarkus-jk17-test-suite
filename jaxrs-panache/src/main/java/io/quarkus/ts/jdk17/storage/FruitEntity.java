package io.quarkus.ts.jdk17.storage;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity(name = "fruit")
public class FruitEntity extends PanacheEntity {
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String description;
}
