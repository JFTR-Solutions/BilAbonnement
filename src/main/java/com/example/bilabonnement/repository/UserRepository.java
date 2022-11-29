package com.example.bilabonnement.repository;

import com.example.bilabonnement.Exceptions.UserNotFoundException;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class UserRepository {

    public UserRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
        }

    }

    public void updateRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic) {

        String queryInsert = ("insert into roles_users (role_id,user_id) values (?,?)");
        String queryDelete = ("delete from roles_users where role_id=? and user_id=?");
        try {
            if (!findRoleById(user.getUserId()).contains("sysadmin") && sysadmin) {
                PreparedStatement psts = conn.prepareStatement(queryInsert);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 1);
                psts.executeUpdate();
            }
            if (findRoleById(user.getUserId()).contains("sysadmin") && !sysadmin) {
                PreparedStatement psts = conn.prepareStatement(queryDelete);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 1);
                System.out.println(psts.executeUpdate());
                psts.executeUpdate();
            }
            if (!findRoleById(user.getUserId()).contains("sales") && sales) {
                PreparedStatement psts = conn.prepareStatement(queryInsert);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 2);
                psts.executeUpdate();
            }
            if (findRoleById(user.getUserId()).contains("sales") && !sales) {
                PreparedStatement psts = conn.prepareStatement(queryDelete);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 2);
                psts.executeUpdate();
            }
            if (!findRoleById(user.getUserId()).contains("front_desk") && front_desk) {
                PreparedStatement psts = conn.prepareStatement(queryInsert);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 3);
                psts.executeUpdate();
            }
            if (findRoleById(user.getUserId()).contains("front_desk") && !front_desk) {
                PreparedStatement psts = conn.prepareStatement(queryDelete);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 3);
                psts.executeUpdate();
            }
            if (!findRoleById(user.getUserId()).contains("mechanic") && mechanic) {
                PreparedStatement psts = conn.prepareStatement(queryInsert);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 4);
                psts.executeUpdate();
            }
            if (findRoleById(user.getUserId()).contains("mechanic") && !mechanic) {
                PreparedStatement psts = conn.prepareStatement(queryDelete);
                psts.setInt(2, user.getUserId());
                psts.setInt(1, 4);
                psts.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateUser(User user) {
        try {
            String queryUpdate = ("UPDATE users SET email=?, username=?, first_name=?, last_name=?,birthdate=?,address=?,phone_number=? WHERE user_id=?");
            PreparedStatement psts = conn.prepareStatement(queryUpdate);

            psts.setString(1, user.getEmail());
            psts.setString(2, user.getUsername());
            psts.setString(3, user.getFirstName());
            psts.setString(4, user.getLastName());
            psts.setString(5, user.getBirthdate());
            psts.setString(6, user.getAddress());
            psts.setString(7, user.getPhoneNumber());
            psts.setInt(8, user.getUserId());

            psts.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

            User user = new User(2, email, password, username, first_name, last_name, birthdate, address, phonenr);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAll() {

        List<User> userList = new ArrayList<>();

        try {
            String queryGetAll = ("SELECT * from users");
            PreparedStatement psts = conn.prepareStatement(queryGetAll);
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
            String queryFindRole = "SELECT role_name FROM roles_users AS ru INNER JOIN users " +
                    "ON ru.user_id=users.user_id INNER JOIN roles ON ru.role_id=roles.role_id " +
                    "WHERE users.user_id=?";
            PreparedStatement psts = conn.prepareStatement(queryFindRole);
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
            String queryFindUser = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement psts = conn.prepareStatement(queryFindUser);

            //indsæt email og password i preparedstatement.
            psts.setString(1, email);
            psts.setString(2, password);

            //execute query som giver svar tilbage fra databasen med information om brugeren.
            ResultSet rs = psts.executeQuery();
            while (rs.next()) {
                int user_id = rs.getInt(1);
                String username = rs.getString(3);
                String first_name = rs.getString(5);
                String last_name = rs.getString(6);
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


    public User findUserbyID(int id) {
        User user = new User();
        try {
            String queryCreate = "SELECT * FROM users WHERE user_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            //indsæt email og password i preparedstatement.
            psts.setInt(1, id);

            //execute query som giver svar tilbage fra databasen med information om brugeren.
            ResultSet rs = psts.executeQuery();
            while (rs.next()) {
                int user_id = rs.getInt(1);
                String email = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String first_name = rs.getString(5);
                String last_name = rs.getString(6);
                String date = rs.getString(7);
                String address = rs.getString(8);
                String phoneNumber = rs.getString(9);

                user.setUserId(user_id);
                user.setEmail(email);
                user.setUsername(username);
                user.setPassword(password);
                user.setFirstName(first_name);
                user.setLastName(last_name);
                user.setBirthdate(date);
                user.setAddress(address);
                user.setPhoneNumber(phoneNumber);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(int id) {
        try {
            String deleteQuery = ("DELETE FROM roles_users where user_id=?");
            PreparedStatement psts = conn.prepareStatement(deleteQuery);
            psts.setInt(1, id);
            psts.executeUpdate();

            String deleteuser = ("DELETE FROM users where user_id=?");
            psts = conn.prepareStatement(deleteuser);
            psts.setInt(1, id);
            psts.executeUpdate();


        } catch (SQLException exception) {
            System.out.println(exception);
        }
    }

}