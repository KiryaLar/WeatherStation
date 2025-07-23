package ru.larkin.consumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.larkin.consumer.util.DateFormatterUtil;
import ru.larkin.shared.event.WeatherForecastEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherConsumer {

    private final DateFormatterUtil dateFormatter;
    private final WeatherAnalyzer weatherAnalyzer;
    private final ConcurrentHashMap<String, List<WeatherForecastEvent>> cityForecasts = new ConcurrentHashMap<>();
    private final AtomicInteger readyCities = new AtomicInteger(0);


    @KafkaListener(topics = "${spring.kafka.topic.weather.name}",
            groupId = "weather-group",
            concurrency = "3")
    public void consume(WeatherForecastEvent event) {
        String city = event.getCity();
        List<WeatherForecastEvent> weatherForecasts = cityForecasts.computeIfAbsent(city, k -> new ArrayList<>());
        weatherForecasts.add(event);

        String formatedDate = dateFormatter.formatDate(event.getDateTime());
        log.info("Получен прогноз погоды: город - {}, {}, {}°C, {} \n" +
                 "-----------------------------------------------------------------------------------",
                event.getCity(),
                formatedDate,
                String.format("%.1f", event.getTemperature()),
                event.getWeatherType().getWeather());

        if (weatherForecasts.size() == 7) {
            readyCities.incrementAndGet();
        }

        if (readyCities.intValue() == 20) {
            weatherAnalyzer.doForecastsAnalytics(cityForecasts);
            cityForecasts.clear();
            readyCities.set(0);
        }
    }
}
