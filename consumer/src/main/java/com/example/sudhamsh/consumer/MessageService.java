package com.example.sudhamsh.consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private RabbitMQListener rabbitMQListener;

    public List<String> getAllMessages() {
        return rabbitMQListener.getMessages();
    }
}