package ru.noleg.scootrent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRentalPointDto(@NotBlank @Size(min = 10, max = 50) String title,
                                   Double latitude,
                                   Double longitude,
                                   @NotBlank @Size(min = 10, max = 100) String address,
                                   Long parentId) {
}
