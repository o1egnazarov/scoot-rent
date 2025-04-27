package ru.noleg.scootrent.dto.rentalPoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateRentalPointDto(Long id,
                                   @NotBlank @Size(min = 5, max = 100) String address,
                                   @NotBlank @Size(min = 5, max = 50) String title,
                                   BigDecimal latitude,
                                   BigDecimal longitude,
                                   Long parentId) {
}
