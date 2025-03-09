package com.MercureIT.HR_Manager.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.MercureIT.HR_Manager.models.User;
import com.MercureIT.HR_Manager.repositories.NewUserRepository;

@Service
public class NewUserService {

    @Autowired
    private NewUserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
