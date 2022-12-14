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
            ConnectionManager.createConnection(System.getenv("JDBCUrl"), System.getenv("JDBCUsername"),
                    System.getenv("JDBCPassword"));
        }

    }

//    Frederik
    public void removeRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic)
            throws CarLeasingException {

        // A list containing all the booleans is created in order to loop through them later.
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(sysadmin);
        booleanList.add(sales);
        booleanList.add(front_desk);
        booleanList.add(mechanic);

        //Same with the roles.
        List<String> roles = new ArrayList<>();
        roles.add("System adminstrator");
        roles.add("Forretningsudvikler");
        roles.add("Dataregistrering");
        roles.add("Skade og udbedring");

/*    In below method we start by getting the rolelist based on the user ID. If a user is found, we create a query to delete the role and user from the roles_users table.
       Next we loop through the rolelist, the roles list above and the booleanlist above. Here we check whether a role has been unchecked and if the user has this role assigned.
       If the answer is yes, we create a preparedstatement, were we insert the role_id (i) + adjust for the list starting at index 0 and the userid to the statement and execute it
       */

        try {
            List<String> roleList = findRoleById(user.getUserId());
            String removeRolesQuery = ("delete from roles_users where role_id=? and user_id=?");
            for (int i = 0; i < roles.size(); i++) {
                if (roleList.contains(roles.get(i)) && !booleanList.get(i)) {
                    PreparedStatement psts = conn.prepareStatement(removeRolesQuery);
                    psts.setInt(2, user.getUserId());
                    psts.setInt(1, i + 1);
                    psts.executeUpdate();
                }
            }


        } catch (CarLeasingException| SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.ROLE_ERROR));
        }
    }

    /*Below method acts the same as above remove method only difference is the insert query & the if statement has been switched around. */
    //Frederik
    public void addRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic)
            throws CarLeasingException {

        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(sysadmin);
        booleanList.add(sales);
        booleanList.add(front_desk);
        booleanList.add(mechanic);

        List<String> roles = new ArrayList<>();
        roles.add("System adminstrator");
        roles.add("Forretningsudvikler");
        roles.add("Dataregistrering");
        roles.add("Skade og udbedring");

        try {
            List<String> roleList = findRoleById(user.getUserId());
            String addRolesQuery = ("insert into roles_users (role_id,user_id) values (?,?)");
            for (int i = 0; i < roles.size(); i++) {
                    if (!roleList.contains(roles.get(i)) && booleanList.get(i)) {
                        PreparedStatement psts = conn.prepareStatement(addRolesQuery);
                        psts.setInt(2, user.getUserId());
                        psts.setInt(1, i + 1);
                        psts.executeUpdate();
                    }
            }

        } catch (CarLeasingException | SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.ROLE_ERROR));
        }
    }

    //FREDERIK
    public void updateUser(User user) throws CarLeasingException {
        try {
            String queryUpdate = ("UPDATE users SET email=?, username=?, first_name=?, last_name=?,birthdate=?," +
                    "address=?,phone_number=? WHERE user_id=?");
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }
    //THOMAS
        public void giveCustomerRole(int id) throws CarLeasingException {
            try {
                String queryInsert = ("insert into roles_users (role_id,user_id) values (?,?)");
                PreparedStatement psts = conn.prepareStatement(queryInsert);
                psts.setInt(1, 5);
                psts.setInt(2, id);
                psts.executeUpdate();
            } catch (CarLeasingException | SQLException ex) {
                throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
            }
        }
    //FREDERIK
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //THOMAS
    public int findUserByUsername(String username) throws CarLeasingException {
        int id = 0;
        try {
            String queryFind = ("SELECT user_id FROM users WHERE username=?");
            PreparedStatement psts = conn.prepareStatement(queryFind);
            psts.setString(1, username);
            ResultSet rs = psts.executeQuery();
            while(rs.next()) {
                id = rs.getInt("user_id");
            }
            return id;
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //THOMAS
    public void createCustomer(String email, String password, String username, String first_name, String last_name, Date birthdate, String address, String phonenr) {
        try {
            String queryCreate = ("INSERT INTO users (user_id,email,username,password,first_name,last_name,birthdate," +
                    "address,phone_number)" +
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

//Frederik
    public List<User> getEmployeesWithoutRole() throws CarLeasingException {

        List<User> userList = new ArrayList<>();

        try {
            String queryGetAll = ("SELECT * FROM users LEFT JOIN roles_users ON users.user_id = roles_users.user_id" +
                    " WHERE roles_users.user_id Is Null;");
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return userList;
    }

    //Frederik
    public List<User> getAllEmployees() throws CarLeasingException {

        List<User> userList = new ArrayList<>();

        try {
            String queryGetAll = ("SELECT * from users WHERE user_id IN (SELECT user_id FROM roles_users WHERE role_id!=5)");
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return userList;
    }
    //Thomas
    public List<User> getAllCustomers() throws CarLeasingException {

        List<User> userList = new ArrayList<>();

        try {
            String queryGetAll = ("SELECT * from users WHERE user_id IN (SELECT user_id FROM roles_users WHERE role_id=5) " +
                    "ORDER BY last_name, first_name");
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return userList;
    }

    //Thomas
    public List<User> getAllByRole(int roleId) throws CarLeasingException {
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

        } catch (CarLeasingException | SQLException ex) {
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

        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }

    }

    //Frederik
    public User findUserByEmailAndPassword(String email, String password) throws CarLeasingException {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        try {
            String queryFindUser = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement psts = conn.prepareStatement(queryFindUser);

            //inds??t email og password i preparedstatement.
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
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.WRONG_LOGIN));
        }
    }

    //Frederik
    public User findUserByEmail(String email) throws CarLeasingException {

        User user = new User();
        user.setEmail(email);

        try {
            String queryFindUser = "SELECT * FROM users WHERE email=?";
            PreparedStatement psts = conn.prepareStatement(queryFindUser);

            //inds??t email og password i preparedstatement.
            psts.setString(1, email);

            //execute query som giver svar tilbage fra databasen med information om brugeren.
            ResultSet rs = psts.executeQuery();
            if (rs.next()) {
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
        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return null;
    }

    //FREDERIK
    public User findUserByID(int id) throws CarLeasingException{
        User user = new User();
        try {
            String queryCreate = "SELECT * FROM users WHERE user_id=?";
            PreparedStatement psts = conn.prepareStatement(queryCreate);

            //inds??t email og password i preparedstatement.
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
        } catch (CarLeasingException | SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //FREDERIK
    public void deleteUser(int id) throws CarLeasingException{
        try {
            String deleteQuery = ("DELETE FROM roles_users where user_id=?");
            PreparedStatement psts = conn.prepareStatement(deleteQuery);
            psts.setInt(1, id);
            psts.executeUpdate();

            String deleteuser = ("DELETE FROM users where user_id=?");
            psts = conn.prepareStatement(deleteuser);
            psts.setInt(1, id);
            psts.executeUpdate();


        } catch (CarLeasingException | SQLException ex) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
    }

    //Thomas
    public boolean checkIfUsernameExists(String username) throws CarLeasingException{
        try {
            String query = "SELECT * FROM users WHERE username=?";
            PreparedStatement psts = conn.prepareStatement(query);
            psts.setString(1, username);
            ResultSet rs = psts.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (CarLeasingException | SQLException e) {
            throw new CarLeasingException(exceptionEnums.get(carExceptionEnum.DATABASE_ERROR));
        }
        return false;

    }

}