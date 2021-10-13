package com.shopaway;

import com.shopaway.resources.UserController;
import com.shopaway.health.TemplateHealthCheck;
import com.shopaway.resources.ShopAwayResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import util.PostgresConn;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
                String productSql = "CREATE TABLE IF NOT EXISTS PRODUCTS " +
                        "(id SERIAL PRIMARY KEY     NOT NULL," +
                        " name           TEXT    NOT NULL, " +
                        " description TEXT, " +
                        " imageIds  TEXT," +
                        " price            INT     NOT NULL, " +
                        " address        CHAR(50), " +
                        " created_on TIMESTAMP NOT NULL, " +
                        " sellerId VARCHAR NOT NULL )";
                stmt.executeUpdate(productSql);
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();
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
        final TemplateHealthCheck healthCheck =
            new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
        environment.jersey().register(userController);
    }

}
