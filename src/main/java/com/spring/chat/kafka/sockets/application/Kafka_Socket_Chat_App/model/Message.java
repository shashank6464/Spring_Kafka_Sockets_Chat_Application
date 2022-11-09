package com.spring.chat.kafka.sockets.application.Kafka_Socket_Chat_App.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String sender;
    private String text;
    private String timestamp;


}
