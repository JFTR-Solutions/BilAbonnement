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

    public List<User> getAll() throws CarLeasingException {
        return userRepository.getAll();
    }

    public User getEmail(String email, String password) throws CarLeasingException {
        return userRepository.findUserByEmail(email, password);
    }

    public User findUserByID(int id) {
        return userRepository.findUserbyID(id);
    }


    public List<String> getRoles(int id) throws CarLeasingException {
        return userRepository.findRoleById(id);
    }

    public void createUser(String email, String password, String username, String first_name, String last_name,
                           Date birthdate, String address, String phonenr) {
        userRepository.createUser(email, password, username, first_name, last_name, birthdate, address, phonenr);
    }

    public void updateRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic) throws CarLeasingException {
        userRepository.addRoles(user, sysadmin, sales, front_desk, mechanic);
    }
    public void removeRoles(User user, boolean sysadmin, boolean sales, boolean front_desk, boolean mechanic) throws CarLeasingException {
        userRepository.removeRoles(user, sysadmin, sales, front_desk, mechanic);
    }

    public void updateUser(User user) throws CarLeasingException {
        userRepository.updateUser(user);

    }

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    public List<User> getAllByRole(int roleId) throws CarLeasingException {
        return userRepository.getAllByRole(roleId);
    }

    public boolean checkIfUsernameExists(String username) {
        return userRepository.checkIfUsernameExists(username);
    }
}
