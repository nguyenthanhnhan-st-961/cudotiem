package com.cudotiem.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	private String sender;
    private String content;
    private String timestamp;
}
