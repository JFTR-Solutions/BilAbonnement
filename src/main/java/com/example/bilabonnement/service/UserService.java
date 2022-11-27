package com.example.bilabonnement.service;

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

    public User findUserByID(int id){
        return userRepository.findUserbyID(id);
    }


    public List<String> getRoles(int id){
        return userRepository.findRoleById(id);
    }

    public User createUser(String email, String password, String username, String first_name, String last_name,
                           String birthdate, String address, String phonenr){
        return userRepository.createUser(email,password,username,first_name,last_name,birthdate,address,phonenr);
    }

 /*   public List<String> roleList(){
       return userRepository.getRoleList();
    }*/

}
