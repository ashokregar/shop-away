package com.shopaway.resources;

import com.codahale.metrics.annotation.Timed;
import com.shopaway.pojo.Image;
import com.shopaway.pojo.Order;
import com.shopaway.pojo.Product;
import com.shopaway.service.OrderService;
import com.shopaway.service.ProductService;
import util.PostgresConn;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderController {
    private final PostgresConn postgresConn;
    private final AtomicLong counter;

    public OrderController(PostgresConn postgresConn ) {
        this.postgresConn = postgresConn;
        this.counter = new AtomicLong();
    }

    @POST
    @Timed
    @Path("/new")
    public Order createOrder(Order order) {
        Connection conn = postgresConn.getConnection();

        System.out.println(order);

        OrderService orderService = new OrderService(conn);
        Order order1 = orderService.insertOrder(order);
        System.out.println(order1);

        postgresConn.commit();
        postgresConn.closeConnection();
        return order1;
    }


    @GET
    @Timed
    @Path("/get")
    public Order getOrder(@QueryParam("id") String id) {
        Connection conn = postgresConn.getConnection();
        System.out.println(id);
        OrderService orderService = new OrderService(conn);
        Order order = orderService.getOrder(id);

        postgresConn.commit();
        postgresConn.closeConnection();

        return  order;
    }

    @GET
    @Timed
    @Path("/all")
    public List<Order> getOrders(@QueryParam("userId") String userId) {
        Connection conn = postgresConn.getConnection();
        System.out.println(userId);
        OrderService orderService = new OrderService(conn);
        List<Order> orders = orderService.getOrdersOfUser(userId);

        postgresConn.commit();
        postgresConn.closeConnection();

        return orders;
    }

}

