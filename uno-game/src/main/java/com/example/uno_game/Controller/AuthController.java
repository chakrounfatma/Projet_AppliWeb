package com.example.uno_game.Controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.uno_game.Config.JwtService;
import com.example.uno_game.model.User;
import com.example.uno_game.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        
        return userRepository.save(user);
    }
    
    

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());
        System.out.println("EMAIL = " + user.getEmail());
        System.out.println("PASSWORD = " + user.getPassword());

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateToken(existingUser.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("prenom", existingUser.getPrenom());
        response.put("email", existingUser.getEmail());
        response.put("scoreTotal", existingUser.getScoreTotal().toString());

        return response;
    }

    
}
