package com.shopaway.service;

import com.shopaway.pojo.Image;
import com.shopaway.pojo.Product;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProductService {

    private Connection conn;

    public ProductService(Connection conn) {
        this.conn = conn;
    }

    public List<String> listFilesForFolder(final File folder) {
        List<String> names = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                names.add(fileEntry.getName());
            }
        }
        return names;
    }

    /**
     * insert multiple products
     */
    public void insertProducts(List<Product> list) {
        String SQL = "INSERT INTO products(id,name, description, seller, price,  rating, category, created_on, image_ids ) "
                + "VALUES(DEFAULT,?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;
            for (Product product : list) {
                String imgSQL = "INSERT INTO images(id, name, created_on ) "
                        + "VALUES(?,?,?)";
                List<String> ids = new ArrayList<>();
                for (String image : product.getImageIds().split(",")){
                    System.out.println(image);
                    String uuid = String.valueOf(UUID.randomUUID());
                    PreparedStatement stmt  = conn.prepareStatement(imgSQL);
                    stmt.setString(1, uuid);
                    stmt.setString(2, image);
                    stmt.setDate(3, new java.sql.Date(product.getCreatedOn().getTime()));
                    stmt.executeUpdate();
                    ids.add(uuid);
                }


                statement.setString(1, product.getName());
                statement.setString(2, product.getDescription());
                statement.setString(3, product.getSeller());
                statement.setInt(4, product.getPrice());
                statement.setString(5, product.getRating());
                statement.setString(6, product.getCategory());
                statement.setDate(7,new java.sql.Date(product.getCreatedOn().getTime()));
                System.out.println("joininng.");
                System.out.println(ids);
                System.out.println("joininngasdasd.");
                statement.setString(8, String.join(",", ids));

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * get products
     */
    public List<Product> queryProducts(String cat) {
        String SQL = "";
        if(cat == null || cat == ""){
            SQL = "SELECT * FROM products";
        }else {
            SQL = "SELECT * FROM products WHERE category='" + cat +"'";
        }
        System.out.println(SQL);
        Statement stmt = null;
        List<Product> products = new ArrayList<>();
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String seller = rs.getString("seller");
                int price = rs.getInt("price");
                String category = rs.getString("category");
                String rating = rs.getString("rating");
                String images = rs.getString("image_ids");
                Date createdOn = rs.getDate("created_on");

                Product product1 = new Product(id, name, description, seller, price, rating, category, createdOn, images);
                products.add(product1);
            }
            return products;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return  null;
    }


    /**
     * get products
     */
    public Product getProductOfImage(String image) {
        String SQL = "SELECT * FROM products WHERE image_ids LIKE '%' || '" + image + "' || '%'";
        System.out.println(SQL);
        Statement stmt = null;
        List<Product> products = new ArrayList<>();
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String seller = rs.getString("seller");
                int price = rs.getInt("price");
                String category = rs.getString("category");
                String rating = rs.getString("rating");
                String images = rs.getString("image_ids");
                Date createdOn = rs.getDate("created_on");

                Product product1 = new Product(id, name, description, seller, price, rating, category, createdOn, images);
                products.add(product1);
            }
            return products.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public Image getImage(String imgId) {
        String SQL = "SELECT * FROM images WHERE id LIKE '%' || '" + imgId + "' || '%'";
        System.out.println(SQL);
        Statement stmt = null;
        List<Image> images = new ArrayList<>();
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while ( rs.next() ) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                Date createdOn = rs.getDate("created_on");

                Image image = new Image(id, name, createdOn);
                images.add(image);
            }
            return images.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

}


