package ru.noleg.scootrent.dto.rentalPoint;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Детальная информация о точке проката")
public record DetailRentalPointDto(Long id,
                                   String title,
                                   BigDecimal latitude,
                                   BigDecimal longitude,
                                   String address,
                                   List<DetailRentalPointDto> children,
                                   List<ScooterDtoWithModel> scooters,
                                   int totalCount) {
}
