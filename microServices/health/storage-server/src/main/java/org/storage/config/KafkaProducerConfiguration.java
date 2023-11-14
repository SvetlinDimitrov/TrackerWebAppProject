package org.storage.config;

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
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.storage.client.FoodView;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    public String servers;

    @Bean
    NewTopic recordCreationTopic() {
        return TopicBuilder.name("storage-filling")
                .build();
    }

    @Bean
    NewTopic recordDeletionTopic() {
        return TopicBuilder.name("storage-removing")
                .build();
    }

    @Bean
    KafkaTemplate<String, FoodView> kafkaTemplateCreation() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}
