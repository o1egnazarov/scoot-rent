package ru.noleg.scootrent.dto.rentalPoint;

import java.math.BigDecimal;
import java.util.List;

public record RentalPointDto(Long id,
                             String title,
                             BigDecimal latitude,
                             BigDecimal longitude,
                             String address,
                             List<RentalPointDto> children) {
}
