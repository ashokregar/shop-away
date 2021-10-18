package com.shopaway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shopaway.pojo.Address;
import com.shopaway.pojo.Order;
import com.shopaway.pojo.Product;
import com.shopaway.pojo.STATUS;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderService {

    private Connection conn;

    public OrderService(Connection conn) {
        this.conn = conn;
    }

    public Order getOrder(String id){
        String SQL = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement stmt = null;
        List<Order> orders = new ArrayList<>();
        try{
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            UserService userService = new UserService(conn);
            while ( rs.next() ) {
                String products_json_str = rs.getString("products_json_str");
                String userId = rs.getString("user_id");
                String addressId = rs.getString("address_id");
                Date createdOn = rs.getDate("created_on");
                STATUS status = STATUS.valueOf(rs.getString("status"));

                Product[] products = objectMapper.readValue(products_json_str,typeFactory.constructArrayType(Product.class));
                System.out.println(products);
                Address address = userService.getAddress(addressId);
                Order order = new Order(products, address, createdOn, status, userId);
                order.setId(id);
                orders.add(order);
            }
            return orders.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getOrdersOfUser(String userId){
        String SQL = "SELECT * FROM orders WHERE user_id = ?";
        PreparedStatement stmt = null;
        List<Order> orders = new ArrayList<>();
        try{
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            UserService userService = new UserService(conn);
            while ( rs.next() ) {
                String products_json_str = rs.getString("products_json_str");
                String id = rs.getString("id");
                String addressId = rs.getString("address_id");
                Date createdOn = rs.getDate("created_on");
                STATUS status = STATUS.valueOf(rs.getString("status"));

                Product[] products = objectMapper.readValue(products_json_str,typeFactory.constructArrayType(Product.class));
                System.out.println(products);
                Address address = userService.getAddress(addressId);
                Order order = new Order(products, address, createdOn, status, userId);
                order.setId(id);
                orders.add(order);
            }
            return orders;
        }catch (SQLException e){
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * insert order
     */
    public Order insertOrder(Order order) {
        String SQL = "INSERT INTO orders(id, products_json_str, address_id, user_id, created_on, status) "
                + "VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            String uuid = String.valueOf(UUID.randomUUID());
            ProductService productService = new ProductService(conn);
            Object[] ids = Arrays.stream(order.getProducts()).map(product -> product.getId()).toArray();
            System.out.println(ids[0].toString());
            List<Product> products = productService.queryProductsFromIds(
                    Arrays.asList(ids).toArray(new String[ids.length])
            );
            for (int i = 0; i < products.size(); i++){
                products.get(i).setQty(order.getProducts()[i].getQty());
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String productJsonStr = objectMapper.writeValueAsString(products);
            statement.setString(1, uuid);
            statement.setString(2, productJsonStr);
            statement.setString(3, order.getAddress().getId());
            statement.setString(4, order.getUserId());
            long millis = System.currentTimeMillis();
            statement.setDate(5, new java.sql.Date(millis));
            statement.setString(6, String.valueOf(STATUS.ORDERED));

            System.out.println(statement);
            statement.execute();
            order.setProducts(products.toArray(new Product[0]));
            order.setId(uuid);
            order.setCreatedOn(new Date(millis));
            return order;
        } catch (SQLException | JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }


}


