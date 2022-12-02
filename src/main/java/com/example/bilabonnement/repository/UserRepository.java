package com.example.bilabonnement.repository;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.exceptions.carExceptionEnum;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.service.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import static com.example.bilabonnement.controllers.LoginController.exceptionEnums;
import static com.example.bilabonnement.service.util.ConnectionManager.conn;

@Repository
public class UserRepository {

    public UserRepository() {
        if (conn == null) {
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"), System.getenv("JDBCPassword"));
        }

    }

    public void removeRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic) throws CarLeasingException {

        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(sysadmin);
        booleanList.add(sales);
        booleanList.add(front_desk);
        booleanList.add(mechanic);

        List<String> roles = new ArrayList<>();
        roles.add("sysadmin");
        roles.add("sales");
        roles.add("front_desk");
        roles.add("mechanic");

        try {
            List<String> roleList = findRoleById(user.getUserId());
            String queryDelete = ("delete from roles_users where role_id=? and user_id=?");
            for (int i = 0; i < roles.size(); i++) {
                if (roleList.contains(roles.get(i)) && !booleanList.get(i)) {
                    PreparedStatement psts = conn.prepareStatement(queryDelete);
                    psts.setInt(2, user.getUserId());
                    psts.setInt(1, i + 1);
                    psts.executeUpdate();
                }
            }


        } catch (CarLeasingException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.ROLE_ERROR));
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public void addRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic) throws CarLeasingException {

        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(sysadmin);
        booleanList.add(sales);
        booleanList.add(front_desk);
        booleanList.add(mechanic);

        List<String> roles = new ArrayList<>();
        roles.add("sysadmin");
        roles.add("sales");
        roles.add("front_desk");
        roles.add("mechanic");

        try {
            List<String> roleList = findRoleById(user.getUserId());
            String queryInsert = ("insert into roles_users (role_id,user_id) values (?,?)");
            for (int i = 0; i < roles.size(); i++) {
                    if (!roleList.contains(roles.get(i)) && booleanList.get(i)) {
                        PreparedStatement psts = conn.prepareStatement(queryInsert);
                        psts.setInt(2, user.getUserId());
                        psts.setInt(1, i + 1);
                        psts.executeUpdate();
                    }
            }

        } catch (CarLeasingException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.ROLE_ERROR));
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }


    public void updateUser(User user) throws CarLeasingException {
        try {
            String queryUpdate = ("UPDATE users SET email=?, username=?, first_name=?, last_name=?,birthdate=?,address=?,phone_number=? WHERE user_id=?");
            PreparedStatement psts = conn.prepareStatement(queryUpdate);

            psts.setString(1, user.getEmail());
            psts.setString(2, user.getUsername());
            psts.setString(3, user.getFirstName());
            psts.setString(4, user.getLastName());
            psts.setDate(5, user.getBirthdate());
            psts.setString(6, user.getAddress());
            psts.setString(7, user.getPhoneNumber());
            psts.setInt(8, user.getUserId());

            psts.executeUpdate();

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public void createUser(String email, String password, String username, String first_name, String last_name, Date birthdate, String address, String phonenr) {
        try {
            String queryCreate = ("INSERT INTO users (user_id,email,username,password,first_name,last_name,birthdate,address,phone_number)" +
                    "VALUES (DEFAULT,?,?,?,?,?,?,?,?)");
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            psts.setString(1, email);
            psts.setString(2, username);
            psts.setString(3, password);
            psts.setString(4, first_name);
            psts.setString(5, last_name);
            psts.setDate(6, birthdate);
            psts.setString(7, address);
            psts.setString(8, phonenr);

            psts.executeUpdate();

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public List<User> getAll() throws CarLeasingException {

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
                Date birthdate = rs.getDate(7);
                String address = rs.getString(8);
                String phoneNumber = rs.getString(9);

                userList.add(new User(userId, email, username, password, firstName, lastName, birthdate, address, phoneNumber));
            }

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return userList;
    }

    //Thomas
    public List<User> getAllByRole(int roleId) {
        List<User> userList = new ArrayList<>();

        try {
            String queryGetAll = ("SELECT * from users WHERE user_id IN (SELECT user_id FROM roles_users WHERE role_id=?)");
            PreparedStatement psts = conn.prepareStatement(queryGetAll);
            psts.setInt(1, roleId);
            ResultSet rs = psts.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt(1);
                String email = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String firstName = rs.getString(5);
                String lastName = rs.getString(6);
                Date birthdate = rs.getDate(7);
                String address = rs.getString(8);
                String phoneNumber = rs.getString(9);

                userList.add(new User(userId, email, username, password, firstName, lastName, birthdate, address, phoneNumber));
            }

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return userList;
    }


    //Frederik
    public List<String> findRoleById(int id) throws CarLeasingException {
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
            roleList.remove("customer");

            return roleList;

        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }

    }

    //Frederik
    public User findUserByEmail(String email, String password) throws CarLeasingException {

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
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.WRONG_LOGIN));
        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }

    }

    //FREDERIK
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
                Date date = rs.getDate(7);
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
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //FREDERIK
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


        } catch (NullPointerException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    public boolean checkIfUsernameExists(String username) {
        try {
            String query = "SELECT * FROM users WHERE username=?";
            PreparedStatement psts = conn.prepareStatement(query);
            psts.setString(1, username);
            ResultSet rs = psts.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return false;

    }

}