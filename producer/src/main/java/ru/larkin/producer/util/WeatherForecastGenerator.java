package ru.larkin.producer.util;

import org.springframework.stereotype.Component;
import ru.larkin.shared.enums.WeatherType;

import java.util.Random;

@Component
public class WeatherForecastGenerator {

    private final Random random = new Random();

    public double generateTemperature() {
        return random.nextDouble(36);
    }

    public WeatherType generateWeatherType() {
        WeatherType[] types = WeatherType.values();
        int length = types.length;
        return types[random.nextInt(length)];
    }

    public String[] getCities() {
        return new String[] {
                "Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург",
                "Казань", "Нижний Новгород", "Челябинск", "Самара", "Омск",
                "Ростов-на-Дону", "Уфа", "Красноярск", "Воронеж", "Пермь",
                "Волгоград", "Краснодар", "Саратов", "Тюмень", "Тольятти",
                "Магадан"
        };
    }
}
