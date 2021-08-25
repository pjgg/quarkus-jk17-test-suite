package io.quarkus.ts.jdk17.resources;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import io.quarkus.ts.jdk17.model.Fruit;

@Path("/fruits")
public class FruitsResource {

    private static final String TEXT_BLOCK_EXAMPLE = """
            JEP 378: Text Blocks
            Summary
            Add text blocks to the Java language. A text block is a multi-line string literal that avoids the need for
            most escape sequences, automatically formats the string in a predictable way, and gives the developer
            control over the format when desired.

            History
            Text blocks were proposed by JEP 355 in early 2019 as a follow-on to explorations begun in JEP 326
            (Raw String Literals), which was initially targeted to JDK 12 but eventually withdrawn and did not appear
             in that release. JEP 355 was targeted to JDK 13 in June 2019 as a preview feature. Feedback on JDK 13
             suggested that text blocks should be previewed again in JDK 14, with the addition of two new escape
             sequences. Consequently, JEP 368 was targeted to JDK 14 in November 2019 as a preview feature. Feedback
             on JDK 14 suggested that text blocks were ready to become final and permanent in JDK 15 with
             no further changes.
            """;

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

    @GET
    @Path("jpe_378")
    @Consumes(TEXT_PLAIN)
    public String simpleMultilineExample() {
        return TEXT_BLOCK_EXAMPLE;
    }

    @GET
    @Path("longestDesc")
    public Fruit longestDescription() {
        return Fruit.getLongestDescription();
    }
}
