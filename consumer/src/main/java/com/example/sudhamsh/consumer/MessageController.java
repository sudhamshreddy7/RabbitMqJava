package com.example.sudhamsh.consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/messages")
    public String getMessages(Model model) {
        model.addAttribute("messages", messageService.getAllMessages());
        return "messages";  // Returns the Thymeleaf template
    }
}