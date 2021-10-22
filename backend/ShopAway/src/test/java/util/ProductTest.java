package util;


import com.shopaway.pojo.Address;
import com.shopaway.pojo.Product;
import com.shopaway.pojo.User;
import com.shopaway.resources.ProductController;
import com.shopaway.resources.UserController;
import com.shopaway.service.ProductService;
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
        ProductService productService = new ProductService(postgresConn.conn);
        List<Product> products = productService.queryProductsFromCategory(null);
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
        ProductService productService = new ProductService(postgresConn.conn);
        System.out.println("category: "+ category);
        List<Product> products = productService.queryProductsFromCategory(category);
        Object[] categories = products.stream().filter(product ->  !product.getCategory().equals(category)).toArray();
        Assertions.assertAll(() -> Assertions.assertTrue(categories.length == 0), () -> Assertions.assertTrue(products.size() > 0));
    }


    @Test
    @Order(3)
    @DisplayName("Should return empty list for a unknown category")
    public void GetProductsWithUnknownCategory(){
        ProductService productService = new ProductService(postgresConn.conn);
        String unKnownCategory = category + "un-known";
        System.out.println("category: "+ unKnownCategory);
        List<Product> products = productService.queryProductsFromCategory(unKnownCategory);
        Assertions.assertTrue(products.size() == 0);
    }



}