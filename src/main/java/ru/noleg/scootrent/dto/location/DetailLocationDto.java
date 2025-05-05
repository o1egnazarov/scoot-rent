package ru.noleg.scootrent.dto.location;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.noleg.scootrent.dto.scooter.ScooterDtoWithModel;
import ru.noleg.scootrent.entity.location.LocationType;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Детальная информация о локации")
public record DetailLocationDto(Long id,
                                String title,
                                LocationType locationType,
                                BigDecimal latitude,
                                BigDecimal longitude,
                                String address,
                                List<ScooterDtoWithModel> scooters,
                                int totalCount) {
}
