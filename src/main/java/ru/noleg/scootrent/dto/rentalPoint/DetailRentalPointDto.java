package ru.noleg.scootrent.dto.rentalPoint;

import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;

import java.math.BigDecimal;
import java.util.List;

public record DetailRentalPointDto(Long id,
                                   String title,
                                   BigDecimal latitude,
                                   BigDecimal longitude,
                                   String address,
                                   List<DetailRentalPointDto> children,
                                   List<ScooterDtoWithModel> scooters,
                                   int totalCount) {
}
