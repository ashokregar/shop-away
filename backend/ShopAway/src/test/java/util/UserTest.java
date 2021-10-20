package util;


import com.shopaway.pojo.Address;
import com.shopaway.pojo.User;
import com.shopaway.resources.UserController;
import com.shopaway.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

public class UserTest {
    private String password = new Date().toString();
    private User user = new User("testUser", "testUserName", password, "88872388388", "test@gemail.com", new Date(), new Date());
    private Address address = new Address("Street1", "City1", "State1", 123456, new Date(), "0b75df4d-f605-4523-88ea-58baabcee635");



    @Test
    public void CreateAndGetUserTest(){
        PostgresConn postgresConn = new PostgresConn();
        Connection conn = postgresConn.getConnection();
        UserController userController = new UserController(postgresConn);

        User user1 = (User) userController.addUser(user);
        Assertions.assertEquals(user1.getUsername(), user.getUsername());

        Map<String, Object> rs = userController.getUser(user1);
        User user2 = (User) rs.get("user");
        Assertions.assertEquals(user2.getUsername(), user.getUsername());


        postgresConn.commit();
        postgresConn.closeConnection();
    }

    @Test
    public void CreateGetDeleteAddressTest(){
        PostgresConn postgresConn = new PostgresConn();
        Connection conn = postgresConn.getConnection();

        // create
        UserController userController = new UserController(postgresConn);
        Address address1 = userController.addAddress(address);
        Assertions.assertNotNull(address1.getId());

        // get
        Address address2 = userController.getAddress(address1.getId());
        Assertions.assertEquals(address2.getId(), address1.getId());

        //delete
        boolean deleted = userController.deleteAddress(address1.getId());
        Assertions.assertTrue(deleted);

        postgresConn.commit();
        postgresConn.closeConnection();
    }




}