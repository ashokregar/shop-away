package com.shopaway.service;

import com.shopaway.pojo.Address;
import com.shopaway.pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    /**
     * get multiple user
     */
    public List<User> queryUser(User userCred) {
        String SQL = "SELECT * FROM users WHERE username=? AND password=?";

        PreparedStatement stmt = null;
        List<User> users = new ArrayList<>();
        try{
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, userCred.getUsername());
            stmt.setString(2, userCred.getPassword());
            System.out.println(stmt);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                Date createdOn = rs.getDate("created_on");
                Date lastLogin = rs.getDate("last_login");

                String lastLoginSQL = "UPDATE users SET last_login=? WHERE username=? AND password=?";
                PreparedStatement stmt1 = conn.prepareStatement(lastLoginSQL);
                stmt1.setDate(1, new Date(System.currentTimeMillis()));
                stmt1.setString(2, username);
                stmt1.setString(3, password);
                System.out.println(stmt1);
                stmt1.execute();

                User user = new User(name, username, password, email, mobile, createdOn, new java.util.Date(System.currentTimeMillis()));
                user.setId(id);
                users.add(user);

            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return  null;
    }

    /**
     * update user
     */
    public User updateUser(User user) {

        String SQL = "INSERT INTO users(id, name, username, password, mobile, email, created_on, last_login) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getMobile());
            statement.setString(6, user.getEmail());
            long millis=System.currentTimeMillis();
            statement.setDate(7, new java.sql.Date(millis));
            statement.setDate(8, new java.sql.Date(millis));

            System.out.println(statement);
            statement.execute();
            user.setId(user.getId());
            user.setCreatedOn(new java.util.Date(millis));
            user.setLastLogin(new java.util.Date(millis));
            return user;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * insert user
     */
    public User insertUser(User user) {

        String SQL = "INSERT INTO users(id, name, username, password, mobile, email, created_on, last_login) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            String uuid = String.valueOf(UUID.randomUUID());
            statement.setString(1, uuid);
            statement.setString(2, user.getName());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getMobile());
            statement.setString(6, user.getEmail());
            long millis=System.currentTimeMillis();
            statement.setDate(7, new java.sql.Date(millis));
            statement.setDate(8, new java.sql.Date(millis));

            System.out.println(statement);
            statement.execute();
            user.setId(uuid);
            user.setCreatedOn(new java.util.Date(millis));
            user.setLastLogin(new java.util.Date(millis));
            return user;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    /**
     * delete user
     */
    public boolean deleteUser(String id) {

        String delSQL = "DELETE FROM users WHERE id=?";
        String getSQL = "SELECT * FROM users WHERE id=?";
        try {
            PreparedStatement ps = conn.prepareStatement(getSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                return false;
            }
            PreparedStatement statement = conn.prepareStatement(delSQL);
            statement.setString(1, id);
            System.out.println(statement);
            statement.execute();
            return true;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return false;
    }

    public Address[] insertAddress(Address[] list){
        String SQL = "INSERT INTO addresses(id, street, state, city, pin, user_id, created_on) "
                + "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;
            for (Address address : list) {
                String uuid = String.valueOf(UUID.randomUUID());
                statement.setString(1, uuid);
                statement.setString(2, address.getStreet());
                statement.setString(3, address.getState());
                statement.setString(4, address.getCity());
                statement.setInt(5, address.getPin());
                statement.setString(6, address.getUserId());
                long millis = System.currentTimeMillis();
                statement.setDate(7, new java.sql.Date(millis));
                address.setId(uuid);
                address.setCreatedOn(new java.util.Date(millis));

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.length) {
                    statement.executeBatch();
                }
            }

            return list;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public Address getAddress(String id) {
        String SQL = "SELECT * FROM addresses WHERE id LIKE '%' || '" + id + "' || '%'";
        System.out.println(SQL);
        Statement stmt = null;
        List<Address> images = new ArrayList<>();
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while ( rs.next() ) {
                String userId = rs.getString("user_id");
                String street = rs.getString("street");
                String state = rs.getString("state");
                String city = rs.getString("city");
                int pin = rs.getInt("pin");
                Date createdOn = rs.getDate("created_on");

                Address address = new Address(street, city, state, pin, createdOn, userId);
                address.setId(id);
                images.add(address);
            }
            return images.get(0);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    public List<Address> getAddressesOfUser(String user_id){
        String SQL = "SELECT * FROM addresses WHERE user_id = ?";
        PreparedStatement stmt = null;
        List<Address> addresses = new ArrayList<>();
        try{
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while ( rs.next() ) {
                String id = rs.getString("id");
                String street = rs.getString("street");
                String state = rs.getString("state");
                String city = rs.getString("city");
                int pin = rs.getInt("pin");
                Date createdOn = rs.getDate("created_on");

                Address address = new Address(street, city, state, pin, createdOn, user_id);
                address.setId(id);
                addresses.add(address);
            }

            return addresses;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteAddress(String id){
        String SQL = "DELETE FROM addresses WHERE id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(SQL);
            statement.setString(1, id);
            statement.execute();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
