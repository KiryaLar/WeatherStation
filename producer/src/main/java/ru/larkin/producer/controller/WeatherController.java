package ru.larkin.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.larkin.producer.dto.ErrorResponse;
import ru.larkin.producer.dto.SuccessResponse;
import ru.larkin.producer.exception.ProducerException;
import ru.larkin.producer.service.WeatherProducer;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherProducer weatherProducer;

    @PostMapping
    public ResponseEntity<Object> generateForecasts() {
        try {
            weatherProducer.sendWeatherForecast();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProducerException(e.getMessage());
        }

        SuccessResponse successResponse = SuccessResponse.builder().timestamp(new Date()).message("Weather forecasts have started to be sent").build();
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ProducerException e) {
        ErrorResponse errorResponse = ErrorResponse.builder().timestamp(new Date()).message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
