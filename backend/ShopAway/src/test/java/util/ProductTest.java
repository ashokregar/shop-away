package util;


import com.shopaway.pojo.Address;
import com.shopaway.pojo.Product;
import com.shopaway.pojo.User;
import com.shopaway.resources.ProductController;
import com.shopaway.resources.UserController;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    private PostgresConn postgresConn;
    private static String category;

    @BeforeAll
    static void setup(){

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
    @DisplayName("Should return all products")
    public void GetAllProductsTest(){
        ProductController productController = new ProductController(postgresConn);
        List<Product> products = productController.getProduct(null);
        Assertions.assertTrue(products.size() > 0);
        int index = (int) (products.size() - (Math.random() * products.size()));
        System.out.println("size: "+ products.size());
        System.out.println("index: "+ index);
        category = products.get(index).getCategory();
    }

    @Test
    @Order(2)
    @DisplayName("Should return all products for a category")
    public void GetProductsWithCategory(){
        ProductController productController = new ProductController(postgresConn);
        System.out.println("category: "+ category);
        List<Product> products = productController.getProduct(category);
        Object[] categories = products.stream().filter(product ->  !product.getCategory().equals(category)).toArray();
        Assertions.assertTrue(categories.length == 0);
    }



}