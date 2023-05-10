package com.cudotiem.messageservice.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.cudotiem.messageservice.model.Message;

@Component
public class MessageListener {

	private static final String TOPIC = "chat";
	
	private static final String groupId = "chatGroup";
	
	@Autowired
	SimpMessagingTemplate template;
	
	@KafkaListener(topics = TOPIC, groupId = groupId)
	public void listen(Message message) {
		template.convertAndSend("/topic/group",message);
	}
}
