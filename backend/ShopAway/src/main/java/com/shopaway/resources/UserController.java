package com.shopaway.resources;

import com.codahale.metrics.annotation.Timed;
import com.shopaway.pojo.User;
import com.shopaway.service.UserService;
import util.PostgresConn;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {
    private final PostgresConn postgresConn;
    private final AtomicLong counter;

    public UserController( PostgresConn postgresConn ) {
        this.postgresConn = postgresConn;
        this.counter = new AtomicLong();
    }

    @POST
    @Timed
    @Path("/get")
    public User getUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println(user.getPassword());

        UserService userService = new UserService(conn);
        List<User> users = userService.queryUser(user);
        System.out.println(users);

        postgresConn.commit();
        postgresConn.closeConnection();
        return users.get(0);
    }

    @POST
    @Timed
    @Path("/create")
    public Object addUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println( user.getCreatedOn());
        UserService userService = new UserService(conn);
        //check if user exist
        List<User> users = userService.queryUser(user);
        if(!users.isEmpty()){
            return false;
        }
        ResultSet resultSet = userService.insertUser(user);
        System.out.println("Before Commit");
        System.out.println(resultSet);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println("After Commit");
        System.out.println(resultSet);
        return user;
    }

}

