package com.github.kshashov.cloud.producer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

public class TestUtils {
    public static <K, V> Consumer<K, V> buildKafkaConsumer(EmbeddedKafkaBroker kafkaBroker, Class<? extends Deserializer> keyDeserializer, Class<? extends Deserializer> valueDeserializer) {
        // Use the procedure documented at https://docs.spring.io/spring-kafka/docs/2.2.4.RELEASE/reference/#embedded-kafka-annotation
        final Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testMetricsEncodedAsSent", "true", kafkaBroker);
        // Since we're pre-sending the messages to test for, we need to read from start of topic
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // We need to match the ser/deser used in expected application config
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer.getName());
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        final DefaultKafkaConsumerFactory<K, V> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        return consumerFactory.createConsumer();
    }
}
