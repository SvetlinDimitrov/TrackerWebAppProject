package org.record.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    public String servers;

    @Bean
    NewTopic recordFirstCreationTopic() {
        return TopicBuilder.name("RECORD_FIRST_CREATION")
                .build();
    }

    @Bean
    NewTopic recordDeletionTopic() {
        return TopicBuilder.name("RECORD_DELETION")
                .build();
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {

        Map<String, Object> props = new HashMap<>();
        String serverToProduce = servers.split(",")[1];
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverToProduce);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}