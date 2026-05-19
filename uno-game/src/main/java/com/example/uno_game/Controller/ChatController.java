package com.example.uno_game.Controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.uno_game.model.Chat;
import com.example.uno_game.model.User;
import com.example.uno_game.repository.ChatRepository;
import com.example.uno_game.repository.UserRepository;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send")
    public Chat sendMessage(
            @RequestParam String senderEmail,
            @RequestParam String email,
            @RequestParam String message
    ) {

        User sender = userRepository.findByEmail(senderEmail);


        User receiver = userRepository.findByEmail(email);

        Chat chat = new Chat();

        chat.setExpediteur(sender);
        chat.setDestinataire(receiver);
        chat.setMessage(message);
        chat.setDate(LocalDateTime.now());

        return chatRepository.save(chat);
    }
    @GetMapping("/messages")
    public List<Chat> getMessages(@RequestParam String email) {
        return chatRepository.findByDestinataire_EmailOrderByDateDesc(email);
    }
}
