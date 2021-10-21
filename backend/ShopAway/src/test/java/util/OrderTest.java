package util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopaway.pojo.Address;
import com.shopaway.pojo.User;
import com.shopaway.resources.OrderController;
import com.shopaway.resources.UserController;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderTest {

    private static PostgresConn postgresConn;
    private static com.shopaway.pojo.Order order;
    private static String password;
    private static User user;
    private static Address address;

    @BeforeAll
    static void setup(){
        password = new Date().toString();
        user = new User("testUser", "testUserName", password, "88872388388", "test@gemail.com", new Date(), new Date());
        order = new com.shopaway.pojo.Order();
        postgresConn = new PostgresConn();
        postgresConn.getConnection();
        UserController userController = new UserController(postgresConn);
        user = (User) userController.addUser(user);
        address = userController.addAddress(new Address("temp", "temp", "test", 12312, new Date(), user.getId()));
        postgresConn.commit();
        postgresConn.closeConnection();
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
        postgresConn = new PostgresConn();
        postgresConn.getConnection();
        UserController userController = new UserController(postgresConn);
        boolean deletedUser = userController.deleteUser(user);
        boolean deletedAddress = userController.deleteAddress(address.getId());
        postgresConn.commit();
        postgresConn.closeConnection();
    }

    @Test
    @Order(1)
    @DisplayName("Creating order for a user")
    public void CreateOrderTest() throws JsonProcessingException {
        OrderController orderController = new OrderController(postgresConn);
        ObjectMapper objectMapper = new ObjectMapper();
        String orderData = "{\"products\":[{\"id\":\"9c083de6-6300-4ad2-8284-eb848b7d309a\",\"qty\":2},{\"id\":\"e8691aac-df60-4a06-8f58-a8e187bf26fa\",\"qty\":1},{\"id\":\"b8917935-53fd-47a3-abcb-184268e9f580\",\"qty\":4}],\"address\":{\"id\":\""+ address.getId() +"\"},\"userId\":\""+ user.getId() +"\"}";
        order = objectMapper.readValue(orderData, com.shopaway.pojo.Order.class);
        com.shopaway.pojo.Order order1 = orderController.createOrder(order);
        Assertions.assertNotNull(order1.getId());
        order = order1;
    }

    @Test
    @Order(2)
    @DisplayName("Should return all orders for a user")
    public void GetAllOrdersForUserTest(){
        OrderController orderController = new OrderController(postgresConn);
        List<com.shopaway.pojo.Order> orders = orderController.getOrders(user.getId());
        Object[] filteredOrders = orders.stream().filter(order2 -> order2.getId().equals(order.getId())).toArray();
        Assertions.assertAll( () -> Assertions.assertTrue(orders.size() > 0), () -> Assertions.assertEquals(((com.shopaway.pojo.Order)filteredOrders[0]).getId(), order.getId()));
    }

    @Test
    @Order(3)
    @DisplayName("Should return order from order id")
    public void GetOrderTest(){
        OrderController orderController = new OrderController(postgresConn);
        com.shopaway.pojo.Order order1 = orderController.getOrder(order.getId());
        Assertions.assertEquals(order1.getId(), order.getId());
    }

}