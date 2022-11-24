package com.example.bilabonnement.service;

import com.example.bilabonnement.models.users.Role;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll(){
        return userRepository.getAll();
    }

    public User getEmail(String email, String password){
        return userRepository.findUserByEmail(email,password);
    }

    public List<String> GetRoles(int id){
        return userRepository.findRoleById(id);
    }

}
