package com.example.sudhamsh.enroll;
import com.example.sudhamsh.enroll.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class EventController {

    private final RabbitTemplate rabbitTemplate;

    public EventController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/")
    public String showEnrollmentForm(Model model) {
        model.addAttribute("event", new Event());
        return "enrollment";
    }

    @PostMapping("/enroll")
    public String enrollEvent(@ModelAttribute Event event, Model model) {
    	System.out.println(event.getEmail()+" "+event.getName());
        // Convert Event object to JSON and send to RabbitMQ
    	try {
            rabbitTemplate.convertAndSend("Event", event);
            model.addAttribute("message", "Successfully enrolled in the event!");
        } catch (Exception e) {
            model.addAttribute("message", "Failed to send event: " + e.getMessage());
        }
        return "enrollment";
    }
}