package com.example.DB;
import com.rabbitmq.client.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class DbApplication implements CommandLineRunner {

    @Value("${rabbitmq.host}")
    private String rabbitMqHost;

    @Value("${rabbitmq.port}")
    private int rabbitMqPort;

    @Value("${rabbitmq.username}")
    private String rabbitMqUsername;

    @Value("${rabbitmq.password}")
    private String rabbitMqPassword;

    @Value("${rabbitmq.queue.name}")
    private String rabbitMqQueue;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Connect to RabbitMQ using properties from application.properties
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMqHost);
        factory.setPort(rabbitMqPort);
        factory.setUsername(rabbitMqUsername);
        factory.setPassword(rabbitMqPassword);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(rabbitMqQueue, true, false, false, null);
            System.out.println("Waiting for messs...");

            // Consume messages from RabbitMQ
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received: " + message);

                // Parse JSON data
                JSONObject jsonData = new JSONObject(message);
                String email = jsonData.getString("email");
                String name = jsonData.getString("name");
                

                // Insert into PostgreSQL if the record doesn't exist
                if (!recordExists(email)) {
                    insertUser(email, name);
                    System.out.println("Inserted record into PostgreSQL.");
                } else {
                    System.out.println("Record with email " + email + " already exists.");
                }
            };

            channel.basicConsume(rabbitMqQueue, true, deliverCallback, consumerTag -> {});
        }
    }

    private boolean recordExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    private void insertUser(String email, String name) {
        String insertQuery = "INSERT INTO users (email, name) VALUES (?, ?)";
        jdbcTemplate.update(insertQuery, email, name);
    }
}
