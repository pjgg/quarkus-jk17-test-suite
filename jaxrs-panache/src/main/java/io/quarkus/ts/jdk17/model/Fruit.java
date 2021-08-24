package io.quarkus.ts.jdk17.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.quarkus.ts.jdk17.storage.FruitEntity;

public record Fruit(
        Long id,
        @NotBlank @Size(max = 20) String name,
        @NotBlank @Size(max = 20) String description) {

    private final static Long UNSET_ID = -1L;

    public Fruit(String name, String description) {
        this(UNSET_ID, name, description);
    }

    public Fruit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static List<Fruit> getAll() {
        return FruitEntity.<FruitEntity> findAll().stream().map(Fruit::fromEntity).collect(Collectors.toList());
    }

    public void save() {
        FruitEntity.persist(toEntity(this));
    }

    public static void deleteById(Long id) {
        FruitEntity.deleteById(id);
    }

    private static Fruit fromEntity(FruitEntity entity) {
        return new Fruit(entity.id, entity.name, entity.description);
    }

    private static FruitEntity toEntity(Fruit fruit) {
        var entity = new FruitEntity();
        if (fruit.id() != null && !fruit.id().equals(UNSET_ID)) {
            entity.id = fruit.id();
        }

        entity.name = fruit.name();
        entity.description = fruit.description();

        return entity;
    }
}
