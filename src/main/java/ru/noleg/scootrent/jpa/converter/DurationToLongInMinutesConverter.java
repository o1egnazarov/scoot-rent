package ru.noleg.scootrent.jpa.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationToLongInMinutesConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return (duration == null) ? null : duration.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long minutes) {
        return (minutes == null) ? null : Duration.ofMinutes(minutes);
    }
}