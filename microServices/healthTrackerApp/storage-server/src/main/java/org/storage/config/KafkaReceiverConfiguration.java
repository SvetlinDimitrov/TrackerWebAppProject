package org.storage.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
@EnableKafka
public class KafkaReceiverConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    public String servers;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerCreation(
            ConsumerFactory<String, String> consumerFactoryCreation) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryCreation);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerDeletion(
            ConsumerFactory<String, String> consumerFactoryDeletion) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryDeletion);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerSingleCreation(
            ConsumerFactory<String, String> consumerFactoryStorageCreationByName) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryStorageCreationByName);
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerSingleDeletion(
            ConsumerFactory<String, String> consumerFactoryStorageSingleDeletion) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryStorageSingleDeletion);
        return factory;
    }

    @Bean
    ConsumerFactory<String, String> consumerFactoryCreation() {

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_three");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());

    }

    @Bean
    ConsumerFactory<String, String> consumerFactoryDeletion() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_four");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());

    }

    @Bean
    ConsumerFactory<String, String> consumerFactoryStorageCreationByName() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_storage_single_creation_one");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());

    }

    @Bean
    ConsumerFactory<String, String> consumerFactoryStorageSingleDeletion() {
        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_storage_single_deletion_one");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());

    }

}
