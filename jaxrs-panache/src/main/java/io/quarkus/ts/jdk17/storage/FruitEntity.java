package io.quarkus.ts.jdk17.storage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.ts.jdk17.model.Fruit;

@Entity(name = "fruit")
@NamedQuery(name = "Fruits.findAllCustom", query = """
        SELECT f FROM fruit f
        ORDER BY f.name DESC
        """, hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
public class FruitEntity extends PanacheEntity {
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String description;

    public static FruitEntity getLongestDescription() {
        return FruitEntity.getEntityManager().createQuery("""
                      SELECT f
                      FROM fruit f
                      WHERE char_length(f.description) = (SELECT max(char_length(description)) FROM fruit )
                """, FruitEntity.class).getSingleResult();
    }
}
