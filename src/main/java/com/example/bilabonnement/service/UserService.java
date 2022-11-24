package com.example.bilabonnement.service;

import com.example.bilabonnement.models.users.Role;
import com.example.bilabonnement.models.users.User;
import com.example.bilabonnement.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getEmail(String email, String password){
        return userRepository.findUserByEmail(email,password);
    }


}
