package ru.noleg.scootrent.dto.rentalPoint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.dto.scooter.UpdateScooterDto;

import java.util.List;

public record UpdateRentalPointDto(@NotBlank @Size(min = 10, max = 50) String title,
                                   Double latitude,
                                   Double longitude,
                                   @NotBlank @Size(min = 10, max = 100) String address,
                                   List<UpdateRentalPointDto> children,
                                   List<UpdateScooterDto> scooters) {
}
