package org.record.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.record.client.dto.RecordCreation;
import org.record.client.dto.StorageCreation;
import org.record.client.dto.StorageDeletion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    public String servers;

    @Bean
    NewTopic recordCreationTopic() {
        return TopicBuilder.name("record-creation")
                .build();
    }

    @Bean
    NewTopic recordDeletionTopic() {
        return TopicBuilder.name("record-deletion")
                .build();
    }

    @Bean
    NewTopic storageCreationTopic() {
        return TopicBuilder.name("storage-creation")
                .build();
    }

    @Bean
    NewTopic storageDeletionTopic() {
        return TopicBuilder.name("storage-deletion")
                .build();
    }

    @Bean
    KafkaTemplate<String, RecordCreation> kafkaTemplateCreation() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean
    KafkaTemplate<String, Long> kafkaTemplateDeletion() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean
    KafkaTemplate<String, StorageCreation> kafkaTemplateStorageCreation() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean
    KafkaTemplate<String, StorageDeletion> kafkaTemplateStorageDeletion() {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }
}
