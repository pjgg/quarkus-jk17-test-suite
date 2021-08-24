package io.quarkus.ts.jdk17.resources;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.quarkus.ts.jdk17.model.Fruit;

@Path("/fruits")
public class FruitsResource {

    @GET
    public List<Fruit> list() {
        return Fruit.getAll();
    }

    @POST
    @Transactional
    public List<Fruit> add(@Valid Fruit fruit) {
        fruit.save();
        return Fruit.getAll();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public List<Fruit> delete(@PathParam("id") Long id) {
        Fruit.deleteById(id);
        return Fruit.getAll();
    }
}
