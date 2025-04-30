package ru.noleg.scootrent.dto.tariff;

import jakarta.validation.constraints.NotNull;

public record SubscribeUserDto(@NotNull Long userId) {
}
