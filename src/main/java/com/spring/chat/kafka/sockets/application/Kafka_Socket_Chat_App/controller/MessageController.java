package com.spring.chat.kafka.sockets.application.Kafka_Socket_Chat_App.controller;

import com.spring.chat.kafka.sockets.application.Kafka_Socket_Chat_App.constant.KafkaConstant;
import com.spring.chat.kafka.sockets.application.Kafka_Socket_Chat_App.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
public class MessageController {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @PostMapping(value = "/api/send", consumes="application/json", produces = "application/json")
    public void send(@RequestBody Message message){
        message.setTimestamp(LocalDateTime.now().toString());
        try{
            kafkaTemplate.send(KafkaConstant.TOPIC, message).get();

        }catch(InterruptedException | ExecutionException e){
            throw new RuntimeException(e);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    //WE-Socket APIs
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message){

        return message;
    }

    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor){
        //try to add user for Web Socket API session ....
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;

    }


}
