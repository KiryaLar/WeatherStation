package ru.larkin.producer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.larkin.producer.exception.ProducerException;
import ru.larkin.producer.util.WeatherForecastGenerator;
import ru.larkin.shared.event.WeatherForecastEvent;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherProducer {

    @Value("${spring.kafka.topic.weather.name}")
    private String topic;

    private final WeatherForecastGenerator weatherForecastGenerator;
    private final KafkaTemplate<String, WeatherForecastEvent> kafkaTemplate;

    public void sendWeatherForecast() {
        LocalDateTime measurementDate = LocalDateTime.of(2025, 7, 14, 12, 0);
        for (int i = 0; i < 7;  i++) {
            for (String city : weatherForecastGenerator.getCities()) {
                WeatherForecastEvent event = WeatherForecastEvent.builder()
                        .weatherType(weatherForecastGenerator.generateWeatherType())
                        .temperature(weatherForecastGenerator.generateTemperature())
                        .city(city)
                        .dateTime(measurementDate)
                        .build();
                kafkaTemplate.send(topic, city, event);
            }

            measurementDate = measurementDate.plusDays(1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new ProducerException(e.getMessage());
            }
        }
    }
}
