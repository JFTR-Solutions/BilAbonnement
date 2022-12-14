package com.example.bilabonnement.service;

import com.example.bilabonnement.exceptions.CarLeasingException;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllEmployees() throws CarLeasingException {
        return userRepository.getAllEmployees();
    }

    public List<User> getEmployeeWithoutRole() throws CarLeasingException {
        return userRepository.getEmployeesWithoutRole();
    }

    public List<User> getAllCustomers() throws CarLeasingException {
        return userRepository.getAllCustomers();
    }


    public User getEmailAndPassword(String email, String password) throws CarLeasingException {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public User getEmail(String email) throws CarLeasingException {
        return userRepository.findUserByEmail(email);
    }

    public User findUserByID(int id) throws CarLeasingException {
        return userRepository.findUserByID(id);
    }


    public List<String> getRoles(int id) throws CarLeasingException {
        return userRepository.findRoleById(id);
    }

    public void createUser(String email, String password, String username, String first_name, String last_name,
                           Date birthdate, String address, String phonenr) throws CarLeasingException{
        userRepository.createUser(email, password, username, first_name, last_name, birthdate, address, phonenr);
    }

    public void createCustomer(String email, String password, String username, String first_name, String last_name,
                           Date birthdate, String address, String phonenr) throws CarLeasingException{
        userRepository.createCustomer(email, password, username, first_name, last_name, birthdate, address, phonenr);
    }

    public void updateRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic)
            throws CarLeasingException {
        userRepository.addRoles(user, sysadmin, sales, front_desk, mechanic);
    }
    public void removeRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic)
            throws CarLeasingException {
        userRepository.removeRoles(user, sysadmin, sales, front_desk, mechanic);
    }

    public void updateUser(User user) throws CarLeasingException {
        userRepository.updateUser(user);

    }

    public void deleteUser(int id) throws CarLeasingException {
        userRepository.deleteUser(id);
    }

    public List<User> getAllByRole(int roleId) throws CarLeasingException {
        return userRepository.getAllByRole(roleId);
    }

    public boolean checkIfUsernameExists(String username) throws CarLeasingException {
        return userRepository.checkIfUsernameExists(username);
    }

    public void giveCustomerRole(int id) throws CarLeasingException {
        userRepository.giveCustomerRole(id);
    }

    public int findUserByUsername(String username) throws CarLeasingException {
        return userRepository.findUserByUsername(username);
    }
}
