package com.sda.peaceverse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.sda.peaceverse.repository.UserRepository;
import com.sda.peaceverse.entity.User;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        if(userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(username, password); // later you can hash the password
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid username or password");
        }
        return optionalUser.get();
    }
}
