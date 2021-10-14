package com.shopaway;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopaway.pojo.Product;
import com.shopaway.resources.ProductController;
import com.shopaway.resources.UserController;
import com.shopaway.health.TemplateHealthCheck;
import com.shopaway.resources.ShopAwayResource;
import com.shopaway.service.ProductService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import util.PostgresConn;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ShopAwayApplication extends Application<ShopAwayConfiguration> {
    public static void main(String[] args) throws Exception {
        new ShopAwayApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ShopAwayConfiguration> bootstrap) {
        // nothing to do yet
        // migration
        PostgresConn postgresConn =  new PostgresConn();
        Connection conn = postgresConn.getConnection();

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            try{
                String userSql = "CREATE TABLE IF NOT EXISTS USERS \n" +
                        "(id SERIAL PRIMARY KEY     NOT NULL," +
                        " name           TEXT    NOT NULL, " +
                        " username       TEXT    NOT NULL, " +
                        " password       TEXT    NOT NULL, " +
                        " email          TEXT    NOT NULL, " +
                        " mobile         TEXT    NOT NULL, " +
                        " created_on     DATE    NOT NULL, " +
                        " last_login     DATE    NOT NULL " +
                        ");";
                stmt.executeUpdate(userSql);
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                String productSql = "CREATE TABLE PRODUCTS " +
                        "(id SERIAL PRIMARY KEY     NOT NULL," +
                        " name           TEXT    NOT NULL, " +
                        " description TEXT, " +
                        " image_ids  TEXT," +
                        " price            INT     NOT NULL, " +
                        " category TEXT     NOT NULL, " +
                        " rating        TEXT, " +
                        " created_on TIMESTAMP NOT NULL, " +
                        " seller VARCHAR NOT NULL )";
                stmt.executeUpdate(productSql);

                String imageSql = "CREATE TABLE IF NOT EXISTS IMAGES " +
                        "(id TEXT PRIMARY KEY     NOT NULL," +
                        " name           TEXT    NOT NULL, " +
                        " created_on TIMESTAMP NOT NULL " +
                        ")";
                stmt.executeUpdate(imageSql);



                ProductService productService = new ProductService(conn);
                // create object mapper instance
                ObjectMapper mapper = new ObjectMapper();

                // convert JSON file to map
                System.out.println("products path");
                System.out.println(Paths.get("assets/products.json"));
                Map<?, ?> map = mapper.readValue(Paths.get("assets/products.json").toFile(), Map.class);

                // print map entries
                List<Product> products = new ArrayList<>();
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + "=" + entry.getValue());
                    String category = (String) entry.getKey();
                    Map<?, ?> map1 = (Map<?, ?>) entry.getValue();
                    for (Map.Entry<?, ?> prod : map1.entrySet()) {
                        String name = (String) prod.getKey();
                        Product product = mapper.convertValue(prod.getValue(), Product.class);
                        product.setCategory(category);
                        product.setName(name);
                        products.add(product);
                        final File imgsFolder = Paths.get("assets/images/"+ name).toFile();
                        System.out.println(imgsFolder);
                        List<String> imgNames = productService.listFilesForFolder(imgsFolder);

                        String ids = String.join(",", imgNames);
                        product.setImageIds(ids);
                    }
                }

                productService.insertProducts(products);
            }catch (Exception e){
//                e.printStackTrace();

            }



        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(e.getErrorCode());
        }

        postgresConn.commit();
        postgresConn.closeConnection();

    }

    @Override
    public void run(ShopAwayConfiguration configuration,
                    Environment environment) {
        final ShopAwayResource resource = new ShopAwayResource(
            configuration.getTemplate(),
            configuration.getDefaultName()
        );
        final UserController userController = new UserController(
                new PostgresConn()
        );
        final ProductController productController = new ProductController(
                new PostgresConn()
        );
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(userController);
        environment.jersey().register(productController);
    }

}
