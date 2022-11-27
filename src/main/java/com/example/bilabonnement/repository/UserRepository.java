package com.example.bilabonnement.repository;

import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class UserRepository {

    public UserRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
        }

    }

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

    public User createUser(String email, String password, String username, String first_name, String last_name, String birthdate, String address, String phonenr) {
            try {
                String queryCreate = ("INSERT INTO users (user_id,email,username,password,first_name,last_name,birthdate,address,phone_number)" +
                        "VALUES (DEFAULT,?,?,?,?,?,?,?,?)");
                PreparedStatement psts = conn.prepareStatement(queryCreate);

                psts.setString(1, email);
                psts.setString(2, username);
                psts.setString(3, password);
                psts.setString(4, first_name);
                psts.setString(5, last_name);
                psts.setString(6, birthdate);
                psts.setString(7, address);
                psts.setString(8, phonenr);

                psts.executeUpdate();

                User user = new User(2,email,password,username,first_name,last_name,birthdate,address,phonenr);
                return user;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    public List<User> getAll() {

        List<User> userList = new ArrayList<>();

        try {
            String queryCreate = ("SELECT * from users");
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            ResultSet rs = psts.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt(1);
                String email = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String firstName = rs.getString(5);
                String lastName = rs.getString(6);
                String birthdate = rs.getString(7);
                String address = rs.getString(8);
                String phoneNumber = rs.getString(9);

                userList.add(new User(userId, email, username, password, firstName, lastName, birthdate, address, phoneNumber));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }


    //Frederik
    public List<String> findRoleById(int id) {
        List<String> roleList = new ArrayList<>();

        try {
            String queryCreate = "SELECT role_name FROM roles_users AS ru INNER JOIN users " +
                    "ON ru.user_id=users.user_id INNER JOIN roles ON ru.role_id=roles.role_id " +
                    "WHERE users.user_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);
            psts.setInt(1, id);

            ResultSet rs = psts.executeQuery();
            while (rs.next()) {
                roleList.add(rs.getString(1));
            }
            return roleList;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
/*    public List<String> getRoleList() {
        return roleList;
    }*/

    //Frederik
    public User findUserByEmail(String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            String queryCreate = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            //indsæt email og password i preparedstatement.
            psts.setString(1, email);
            psts.setString(2, password);

            //execute query som giver svar tilbage fra databasen med information om brugeren.
            ResultSet rs = psts.executeQuery();
            while (rs.next()) {
                int user_id = rs.getInt(1);
                String username = rs.getString(2);
                String first_name = rs.getString(4);
                String last_name = rs.getString(5);
                String date = rs.getString(7);
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
