package ru.larkin.shared.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.larkin.shared.enums.WeatherType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastEvent {
    private WeatherType weatherType;
    private Double temperature;
    private String city;
    private LocalDateTime dateTime;
}
