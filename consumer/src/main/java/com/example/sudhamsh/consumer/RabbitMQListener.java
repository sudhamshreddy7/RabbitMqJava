package com.example.sudhamsh.consumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RabbitMQListener {
	private List<String> messages = new ArrayList<>();

    // Listen to the "Event" queue
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        messages.add(message);  // Store message in memory
    }

    public List<String> getMessages() {
        return messages;
    }
}
