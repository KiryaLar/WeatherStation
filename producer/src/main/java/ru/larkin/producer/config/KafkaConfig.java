package ru.larkin.producer.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Bean
    public NewTopic weatherForecastTopic(@Value("${spring.kafka.topic.weather.name}") String topic,
                                         @Value("${spring.kafka.topic.weather.partitions}") int partitions,
                                         @Value("${spring.kafka.topic.weather.replicas}") int replicas,
                                         @Value("${spring.kafka.topic.weather.configs.min.insync.replicas}") String minInSyncReplicas) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replicas)
                .config("min.insync.replicas", minInSyncReplicas)
                .build();
    }

    @Bean
    public NewTopic weatherForecastDltTopic() {
        return TopicBuilder.name("weather-forecast-topic-dlt")
                .partitions(3)
                .replicas(3)
                .config("min.insync.replicas", "2")
                .build();
    }
}
