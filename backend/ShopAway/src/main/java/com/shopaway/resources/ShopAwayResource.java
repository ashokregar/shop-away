package com.shopaway.resources;

import com.shopaway.api.Saying;
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class ShopAwayResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public ShopAwayResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }



}
