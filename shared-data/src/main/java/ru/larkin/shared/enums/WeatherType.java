package ru.larkin.shared.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeatherType {
    SUNNY("Солнечно"),
    CLOUDY("Облачно"),
    RAINY("Дождливо"),
    SNOWY("Снежно");

    private final String weather;
}
