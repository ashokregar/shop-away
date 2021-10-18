package util;


import com.shopaway.pojo.Product;
import com.shopaway.pojo.User;
import com.shopaway.resources.ProductController;
import com.shopaway.resources.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

public class ProductTest {

    @Test
    public void GetAllProductsTest(){
        PostgresConn postgresConn = new PostgresConn();
        Connection conn = postgresConn.getConnection();
        ProductController productController = new ProductController(postgresConn);
        List<Product> products = productController.getProduct("");
        Assertions.assertTrue(products.size() > 0);
        postgresConn.commit();
        postgresConn.closeConnection();
    }

    @Test
    public void GetProductsWithCategory(){
        PostgresConn postgresConn = new PostgresConn();
        Connection conn = postgresConn.getConnection();
        ProductController productController = new ProductController(postgresConn);
        List<Product> products = productController.getProduct("Hand Blenders");
        Object[] categories = products.stream().filter(product ->  !product.getCategory().equals("Hand Blenders")).toArray();
        Assertions.assertTrue(categories.length == 0);
        postgresConn.commit();
        postgresConn.closeConnection();
    }




}