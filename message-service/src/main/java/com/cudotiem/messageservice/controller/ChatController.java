package com.cudotiem.messageservice.controller;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cudotiem.messageservice.model.Message;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

	private static final String CHAT_TOPIC = "chat";
	@Autowired
	private KafkaTemplate<String, Message> kafkaTemplate;
	
	@PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
	public void sendMessage(@RequestBody Message message) {
		message.setTimestamp(LocalDateTime.now().toString());
		try {
			kafkaTemplate.send(CHAT_TOPIC, message).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	 @MessageMapping("/sendMessage")
	    @SendTo("/topic/group")
	    public Message broadcastGroupMessage(@Payload Message message) {
	        //Sending this message to all the subscribers
	        return message;
	    }
}
