package com.example.kafka.embedded.example;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
@EnableBinding(TheBinding.class)
public class ExampleApplicationUsingKStream {

	public static void main(String[] args) {
        SpringApplication.run(ExampleApplicationUsingKStream.class, args);
    }
	
	@StreamListener
    @SendTo(TheBinding.OUTPUT)
    public KStream<String,String> toUpperCase (@Input(TheBinding.INPUT) KStream<String,String> in){
    	return in.map((cle, valeur) -> KeyValue.pair(cle, valeur.toUpperCase()));
    }
	
}

