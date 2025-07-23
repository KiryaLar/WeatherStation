package ru.larkin.consumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.larkin.consumer.util.DateFormatterUtil;
import ru.larkin.shared.enums.WeatherType;
import ru.larkin.shared.event.WeatherForecastEvent;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherAnalyzer {

    private final DateFormatterUtil dateFormatterUtil;

    public void doForecastsAnalytics(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        if (cityForecasts.isEmpty()) {
            log.warn("Ошибка при анализе: нет данных для анализа");
            return;
        }

        log.info("\n--------Некоторая аналитика по погоде в 20-ти городах:--------\n");
        calculateMaxNumRainyDays(cityForecasts);
        printSeparatingLine();
        calculateMinNumCloudyDays(cityForecasts);
        printSeparatingLine();
        calculateHottestDay(cityForecasts);
        printSeparatingLine();
        calculateColdestDay(cityForecasts);
        printSeparatingLine();
        calculateHighestAvgTemp(cityForecasts);
        printSeparatingLine();
        calculateAvgSnowyDays(cityForecasts);
        printSeparatingLine();
    }

    public void calculateMaxNumRainyDays(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        int maxNumRainyDays = 0;
        String winningCity = null;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            int rainyDaysInCity = 0;
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                if (dailyForecast.getWeatherType().equals(WeatherType.RAINY)) {
                    rainyDaysInCity++;
                }
            }
            if (rainyDaysInCity > maxNumRainyDays) {
                maxNumRainyDays = rainyDaysInCity;
                winningCity = forecastsForWeek.getFirst().getCity();
            }
        }

        if (winningCity == null) {
            log.info("Ни в одном городе ни разу не было дождя за неделю");
        } else {
            log.info("Наибольшее количество дождливых дней за неделю в городе {}: {} дней лил дождь", winningCity, maxNumRainyDays);
        }
    }

    public void calculateMinNumCloudyDays(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        int minNumCloudyDays = Integer.MAX_VALUE;
        String winningCity = null;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            int cloudyDaysInCity = 0;
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                if (dailyForecast.getWeatherType().equals(WeatherType.CLOUDY)) {
                    cloudyDaysInCity++;
                }
            }
            if (cloudyDaysInCity < minNumCloudyDays) {
                minNumCloudyDays = cloudyDaysInCity;
                winningCity = forecastsForWeek.getFirst().getCity();
            }
        }

        if (winningCity == null) {
            log.warn("Ошибка при подсчете облачных дней");
        } else {
            log.info("Наименьшее количество облачных дней за неделю в городе {}: {} дней не было облачной погоды", winningCity, 7 - minNumCloudyDays);
        }
    }

    public void calculateHottestDay(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        double highestTemp = -100;
        WeatherForecastEvent winner = null;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                if (dailyForecast.getTemperature() > highestTemp) {
                    highestTemp = dailyForecast.getTemperature();
                    winner = dailyForecast;
                }
            }
        }

        if (winner == null) {
            log.info("Во всех городах в каждый день недели было: 0°C");
        } else {
            String formattedDate = dateFormatterUtil.formatDate(winner.getDateTime());
            log.info("Самый жаркий день за неделю был {}, в городе {}: {}°C", formattedDate, winner.getCity(), String.format("%.1f", highestTemp));
        }
    }

    public void calculateColdestDay(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        double lowestTemp = 100;
        WeatherForecastEvent winner = null;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                if (dailyForecast.getTemperature() < lowestTemp) {
                    lowestTemp = dailyForecast.getTemperature();
                    winner = dailyForecast;
                }
            }
        }

        if (winner == null) {
            log.warn("Ошибка при расчете самого холодного дня");
        } else {
            String formattedDate = dateFormatterUtil.formatDate(winner.getDateTime());
            log.info("Самый холодный день за неделю был {}, в городе {}: {}°C", formattedDate, winner.getCity(), String.format("%.1f", lowestTemp));
        }
    }

    public void calculateHighestAvgTemp(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        double highestAvgTemp = 0;
        String winningCity = null;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            double totalCountTemp = 0;
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                totalCountTemp += dailyForecast.getTemperature();
            }

            double avgTemp = totalCountTemp / 7;
            if (avgTemp > highestAvgTemp) {
                highestAvgTemp = avgTemp;
                winningCity = forecastsForWeek.getFirst().getCity();
            }
        }

        if (winningCity == null) {
            log.warn("Ошибка при расчете наибольшей средней температуры");
        } else {
            log.info("Наибольшая средняя температура за неделю была в городе {}: {}°C", winningCity, String.format("%.1f", highestAvgTemp));
        }
    }

    public void calculateAvgSnowyDays(Map<String, List<WeatherForecastEvent>> cityForecasts) {
        int totalSnowyDays = 0;

        for (List<WeatherForecastEvent> forecastsForWeek : cityForecasts.values()) {
            for (WeatherForecastEvent dailyForecast : forecastsForWeek) {
                if (dailyForecast.getWeatherType().equals(WeatherType.SNOWY)) {
                    totalSnowyDays++;
                }
            }
        }

        int avgSnowyDays = totalSnowyDays / 20;
        log.info("Среднее количество снежных дней по всем городам за неделю: {} дней", avgSnowyDays);
    }

    private void printSeparatingLine() {
        log.info("-------------------------------------------------------------------------------------------");
    }
}
