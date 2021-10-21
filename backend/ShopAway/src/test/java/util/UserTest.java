package util;


import com.shopaway.pojo.Address;
import com.shopaway.pojo.User;
import com.shopaway.resources.UserController;
import com.shopaway.service.UserService;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
    private static String password;
    private static User user ;
    private static Address address ;
    private PostgresConn postgresConn;

    @BeforeAll
    static void setup(){
        password = new Date().toString();
        user = new User("testUser", "testUserName", password, "88872388388", "test@gemail.com", new Date(), new Date());
        address = new Address("Street1", "City1", "State1", 123456, new Date(), "0b75df4d-f605-4523-88ea-58baabcee635");

    }

    @BeforeEach
    public void openSession(){
        postgresConn = new PostgresConn();
        postgresConn.getConnection();
        System.out.println("Connection created");

    }

    @AfterEach
    public void closeSession(){
        postgresConn.commit();
        postgresConn.closeConnection();
        System.out.println("Connection closed");
    }

    @AfterAll
    static void tearDown(){
    }


    @Test
    @Order(1)
    @DisplayName("Creating User")
    public void CreateUserTest(){
        UserController userController = new UserController(postgresConn);
        User user1 = (User) userController.addUser(user);
        Assertions.assertEquals(user1.getUsername(), user.getUsername());
        user = user1;
    }

    @Test
    @Order(2)
    @DisplayName("Reading User")
    public void ReadUserTest(){
        UserController userController = new UserController(postgresConn);
        Map<String, Object> rs = userController.getUser(user);
        User user2 = (User) rs.get("user");
        Assertions.assertEquals(user2.getUsername(), user.getUsername());
    }

    @Test
    @Order(3)
    @DisplayName("Updating EmailId of User")
    public void UpdateUserTest(){
        UserController userController = new UserController(postgresConn);
        String newEmail = "abcd@gmail.com";
        user.setEmail(newEmail);
        User user1 = userController.updateUser(user);
        Assertions.assertEquals(user1.getEmail(), newEmail);
    }

    @Test
    @Order(4)
    @DisplayName("Deleting User")
    public void DeleteUserTest(){
        UserController userController = new UserController(postgresConn);
        boolean result = userController.deleteUser(user);
        Assertions.assertTrue(result);
    }


    @Test
    @Order(5)
    @DisplayName("Creating Address")
    public void CreateAddressTest(){
        // create
        UserController userController = new UserController(postgresConn);
        Address address1 = userController.addAddress(address);
        Assertions.assertNotNull(address1.getId());
        address = address1;
    }

    @Test
    @Order(6)
    @DisplayName("Getting Address")
    public void GetAddressTest(){
        // get
        UserController userController = new UserController(postgresConn);
        Address address2 = userController.getAddress(address.getId());
        Assertions.assertEquals(address2.getId(), address.getId());
    }

    @Test
    @Order(7)
    @DisplayName("Deleting Address")
    public void DeleteAddressTest(){
        //delete
        UserController userController = new UserController(postgresConn);
        boolean deleted = userController.deleteAddress(address.getId());
        Assertions.assertTrue(deleted);
    }

}