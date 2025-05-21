package ru.noleg.scootrent.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.noleg.scootrent.dto.rental.ScooterRentalHistoryDto;
import ru.noleg.scootrent.dto.rental.UserRentalHistoryDto;
import ru.noleg.scootrent.entity.rental.Rental;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RentalHistoryMapper {
    @Mapping(target = "rentalId", source = "id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "startPointTitle", source = "startPoint.title")
    @Mapping(target = "endPointTitle", source = "endPoint.title")
    @Mapping(target = "tariffTitle", source = "tariff.title")
    @Mapping(target = "durationOfUsedScooter", source = "scooter.durationInUsed")
    @Mapping(target = "duration", source = "rental.durationOfRental")
    ScooterRentalHistoryDto mapToScooterRentalDto(Rental rental);

    List<ScooterRentalHistoryDto> mapToScooterRentalDtos(List<Rental> rentals);

    @Mapping(target = "rentalId", source = "id")
    @Mapping(target = "startPointTitle", source = "startPoint.title")
    @Mapping(target = "endPointTitle", source = "endPoint.title")
    @Mapping(target = "tariffTitle", source = "tariff.title")
    @Mapping(target = "duration", source = "rental.durationOfRental")
    UserRentalHistoryDto mapToUserRentalDto(Rental rental);

    List<UserRentalHistoryDto> mapToUserRentalDtos(List<Rental> rentals);
}
