package ru.larkin.consumer.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class DateFormatterUtil {

    private final Locale russianLocale = Locale.forLanguageTag("ru-RU");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String formatDate(LocalDateTime dateTime) {
        String dayOfWeek = dateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, russianLocale);
        String date = dateTime.format(dateTimeFormatter);
        return String.format("%s-%s", date, dayOfWeek);
    }
}
