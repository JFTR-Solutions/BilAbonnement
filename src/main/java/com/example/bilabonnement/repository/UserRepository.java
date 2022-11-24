package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.users.Role;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;

 @Repository
public class UserRepository {

    @Value("${spring.datasource.url}")
    private String db_url;

    @Value("${spring.datasource.username}")
    private String uid;

    @Value("${spring.datasource.password}")
    private String pwd;


/*    public User createnewUser(String email, String password, String name){

        if (findUserByEmail(email,password) != null) {
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);

        try {
            Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
            String queryCreate = "insert into users (email,password,name) values (?,?,?)";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            //indsæt name og price i prepared statement
            psts.setString(1, user.getEmail());
            psts.setString(2, user.getPassword());
            psts.setString(3, user.getName());

            psts.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        System.out.println(user);
        return user;
    }*/


    public User findUserByEmail(String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            Connection conn = ConnectionManager.getConnection(db_url, uid, pwd);
            String queryCreate = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            //indsæt name og price i prepared statement
            psts.setString(1, email);
            psts.setString(2, password);

            //execute query
            ResultSet rs = psts.executeQuery();
            while (rs.next()){
            int user_id = rs.getInt(1);
            String username = rs.getString(2);
            String first_name = rs.getString(4);
            String last_name = rs.getString(5);
            Date date = rs.getDate(7);
            String address = rs.getString(8);
            String phoneNumber = rs.getString(9);

            user.setUserId(user_id);
            user.setUsername(username);
            user.setFirstName(first_name);
            user.setLastName(last_name);
            user.setBirthdate(date);
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);

                return user;
            }

            return null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }


}
