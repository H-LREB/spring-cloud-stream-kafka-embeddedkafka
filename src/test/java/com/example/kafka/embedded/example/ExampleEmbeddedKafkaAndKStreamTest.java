package com.example.kafka.embedded.example;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExampleEmbeddedKafkaAndKStreamTest {
	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, false, "so0544in","so0544out");
	
	@Autowired
	private KafkaTemplate<Integer, byte[]> template;
	
	@Autowired
    private KafkaProperties properties;
    
    private static Consumer consumer;
    
    @BeforeClass
    public static void setup() throws Exception{
        System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
        System.setProperty("spring.cloud.stream.kafka.binder.zkNodes", embeddedKafka.getZookeeperConnectionString());
        System.setProperty("server.port","0");
        System.setProperty("spring.jmx.enabled" , "false");
        
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group-id", "false", embeddedKafka);
		
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
		consumer = cf.createConsumer();
		embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "so0544out");
        
    }
    
    @After
	public void tearDown() {
		if (consumer != null){
			consumer.close();
		}
	}

    @Test
    public void testSendReceive() {
        template.send("so0544in", "foo".getBytes());
        
        Map<String, Object> configs = properties.buildConsumerProperties();
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "test0544");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        
        ConsumerRecord<String, String> cr = KafkaTestUtils.getSingleRecord(consumer, "so0544out");
		
		System.out.println("Content of output topic : " + cr.value());
		
		assertEquals(cr.value(), "FOO");
    }
    
    

}