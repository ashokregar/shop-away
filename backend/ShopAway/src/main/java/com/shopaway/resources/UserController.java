package com.shopaway.resources;

import com.codahale.metrics.annotation.Timed;
import com.shopaway.pojo.Address;
import com.shopaway.pojo.User;
import com.shopaway.service.UserService;
import util.PostgresConn;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Map<String, Object> getUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println(user.getPassword());

        UserService userService = new UserService(conn);
        User user1 = userService.queryUser(user);
        System.out.println(user1);
        List<Address> addresses = userService.getAddressesOfUser(user1.getId());
        System.out.println(addresses);

        postgresConn.commit();
        postgresConn.closeConnection();
        Map <String, Object> result = new HashMap<>();
        result.put("user", user1);
        result.put("addresses", addresses);
        return  result;
    }

    @POST
    @Timed
    @Path("/create")
    public Object addUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println( user);
        UserService userService = new UserService(conn);
        //check if user exist
        User user1 = userService.queryUser(user);
        if(user1 == null){
            return "User already exist!";
        }
        user1 = userService.insertUser(user);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println(user1);
        return user1;
    }

    @POST
    @Timed
    @Path("/update")
    public User updateUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println( user.getCreatedOn());
        UserService userService = new UserService(conn);
        //check if user exist
        User user1 = userService.queryUser(user);
        if(user1 == null){
            return null;
        }
        user1 = userService.updateUser(user);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println(user1);
        return user;
    }

    @POST
    @Timed
    @Path("/delete")
    public boolean deleteUser(User user) {
        Connection conn = postgresConn.getConnection();
        System.out.println(user);
        UserService userService = new UserService(conn);
        //check if user exist
        boolean deleted = userService.deleteUser(user.getId());

        postgresConn.commit();
        postgresConn.closeConnection();
        return deleted;
    }

    @POST
    @Timed
    @Path("/address/add")
    public Address addAddress(Address address) {
        Connection conn = postgresConn.getConnection();
        System.out.println(address);
        UserService userService = new UserService(conn);

        Address address1 = userService.insertAddress(address);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println(address1);
        return address1;
    }

    @GET
    @Timed
    @Path("/address/get")
    public Address getAddress(String id) {
        Connection conn = postgresConn.getConnection();
        System.out.println(id);
        UserService userService = new UserService(conn);
        Address address = userService.getAddress(id);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println(address);
        return address;
    }

    @POST
    @Timed
    @Path("/address/delete")
    public boolean deleteAddress(String id) {
        Connection conn = postgresConn.getConnection();
        System.out.println( id);
        UserService userService = new UserService(conn);

        boolean deleted = userService.deleteAddress(id);
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println(deleted);
        return deleted;
    }


}

