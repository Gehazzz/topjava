package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Gena on 19.07.2016.
 */
public class SpringTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(s, formatter);
    }
}
