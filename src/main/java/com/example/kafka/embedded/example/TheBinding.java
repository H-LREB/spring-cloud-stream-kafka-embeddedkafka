package com.example.kafka.embedded.example;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface TheBinding {

	String INPUT = "input";
	String OUTPUT = "output";
			
	@Input(INPUT)
	KStream<String, String> messagesIn();
	
	@Output(OUTPUT)
	KStream<String, String> messagesOut();
}


