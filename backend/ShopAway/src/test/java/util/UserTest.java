package util;


import com.shopaway.pojo.User;
import com.shopaway.resources.UserController;
import com.shopaway.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

public class UserTest {
    private String password = new Date().toString();
    private User user = new User("testUser", "testUserName", password, "88872388388", "test@gemail.com", new Date(), new Date());

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




}