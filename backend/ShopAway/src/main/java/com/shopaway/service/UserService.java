package com.shopaway.service;

import com.shopaway.pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection conn;

    public UserService(Connection conn) {
        this.conn = conn;
    }

    /**
     * get multiple user
     */
    public List<User> queryUser(User userCred) {
        String SQL = "SELECT * FROM users WHERE username='" + userCred.getUsername() + "' AND password='"+ userCred.getPassword() + "'";
        System.out.println(SQL);
        Statement stmt = null;
        List<User> users = new ArrayList<>();
        try{
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");
                Date createdOn = rs.getDate("created_on");
                Date lastLogin = rs.getDate("last_login");

                User user = new User(id, name, username, password, email, mobile, createdOn, lastLogin);
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            e.printStackTrace();
        }

        return  null;
    }

    /**
     * insert user
     */
    public ResultSet insertUser(User user) {

        String SQL = "INSERT INTO users(id, name, username, password, mobile, email, created_on, last_login) "
                + "VALUES(DEFAULT,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement = conn.prepareStatement(SQL);

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getMobile());
            statement.setString(5, user.getEmail());
            statement.setDate(6, new java.sql.Date(user.getCreatedOn().getTime()));
            statement.setDate(7, new java.sql.Date(user.getLastLogin().getTime()));


            System.out.println(statement);
            return statement.executeQuery();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }
}
