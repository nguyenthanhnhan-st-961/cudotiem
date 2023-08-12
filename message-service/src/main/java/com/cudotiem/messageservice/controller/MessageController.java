package com.cudotiem.messageservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
	
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/messages")
	public String send(String name) {
		return "Hello " + name;
	}
}
