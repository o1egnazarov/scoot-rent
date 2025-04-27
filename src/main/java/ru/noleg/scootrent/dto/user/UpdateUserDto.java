package ru.noleg.scootrent.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import ru.noleg.scootrent.validator.annotation.Adult;
import ru.noleg.scootrent.validator.annotation.CorrectUsername;
import ru.noleg.scootrent.validator.annotation.NullablePhone;

import java.time.LocalDate;

public record UpdateUserDto(@CorrectUsername @Size(max = 50) String username,
                            @Email @Size(max = 50) String email,
                            @NullablePhone @Size(max = 16) String phone,
                            @Adult LocalDate dateOfBirth) {
}
