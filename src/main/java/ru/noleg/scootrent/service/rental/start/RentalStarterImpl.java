package ru.noleg.scootrent.service.rental.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.entity.rental.Rental;
import ru.noleg.scootrent.entity.rental.RentalStatus;
import ru.noleg.scootrent.entity.scooter.Scooter;
import ru.noleg.scootrent.entity.scooter.ScooterStatus;
import ru.noleg.scootrent.entity.tariff.Tariff;
import ru.noleg.scootrent.entity.user.User;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.exception.UserNotFoundException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.repository.RentalRepository;
import ru.noleg.scootrent.repository.ScooterRepository;
import ru.noleg.scootrent.repository.UserRepository;
import ru.noleg.scootrent.service.rental.tariffselect.TariffSelectionService;

import java.time.LocalDateTime;

@Service
public class RentalStarterImpl implements RentalStarter {

    private static final Logger logger = LoggerFactory.getLogger(RentalStarterImpl.class);

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;
    private final LocationRepository locationRepository;
    private final TariffSelectionService tariffSelectionService;

    public RentalStarterImpl(RentalRepository rentalRepository,
                             UserRepository userRepository,
                             ScooterRepository scooterRepository,
                             LocationRepository locationRepository,
                             TariffSelectionService tariffSelectionService) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
        this.locationRepository = locationRepository;
        this.tariffSelectionService = tariffSelectionService;
    }

    @Override
    @Transactional
    public Long startRental(Long userId, Long scooterId, Long startPointId) {
        logger.info("Начало аренды. Пользователь: {}, Самокат: {}, Точка: {}", userId, scooterId, startPointId);

        Scooter scooter = this.validateAndGetScooter(scooterId);
        User user = this.validateAndGetUser(userId);
        LocationNode startPoint = this.validateAndGetLocation(startPointId);

        this.validateStartRental(userId, startPoint, scooter);

        Tariff tariff = this.selectTariff(userId);

        // TODO проследить за самокатом (за его сохранением с новым статусом)
        scooter.setStatus(ScooterStatus.TAKEN);

        Long rentalId = this.createRental(user, scooter, tariff, startPoint);
        logger.info("Аренда успешно создана. id: {}.", rentalId);
        return rentalId;
    }

    private Tariff selectTariff(Long userId) {
        try {
            return this.tariffSelectionService.selectTariffForUser(userId);
        } catch (Exception e) {
            logger.error("Ошибка в выборе тарифа для пользователя с id: {}", userId, e);
            throw new ServiceException("Ошибка в сервисе выбора тарифа для пользователя с id: " + userId, e);
        }
    }

    private Scooter validateAndGetScooter(Long scooterId) {
        Scooter scooter = this.scooterRepository.findById(scooterId).orElseThrow(
                () -> {
                    logger.error("Самокат с id: {} не найден.", scooterId);
                    return new NotFoundException("Scooter with id: " + scooterId + " not found.");
                }
        );

        if (scooter.getStatus() != ScooterStatus.FREE) {
            logger.warn("Самокат с id: {} не свободен. Текущий статус: {}.", scooterId, scooter.getStatus());
            throw new BusinessLogicException("Scooter with id: " + scooterId + " is not free.");
        }
        return scooter;
    }

    private User validateAndGetUser(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> {
                    logger.error("Пользователь с id: {} не найден.", userId);
                    return new UserNotFoundException("User with id: " + userId + " not found.");
                }
        );
    }

    private LocationNode validateAndGetLocation(Long startPointId) {
        LocationNode startPoint = this.locationRepository.findById(startPointId).orElseThrow(
                () -> {
                    logger.error("Локация с id: {} не найдена.", startPointId);
                    return new NotFoundException("Rental point with id: " + startPointId + " not found.");
                }
        );

        if (startPoint.getLocationType() != LocationType.RENTAL_POINT) {
            logger.warn("Локация с id: {} не является точкой аренды. Ее тип: {}.", startPointId, startPoint.getLocationType());
            throw new BusinessLogicException("Location is not a rental point.");
        }
        return startPoint;
    }

    private void validateStartRental(Long userId, LocationNode startPoint, Scooter scooter) {
        if (this.rentalRepository.isActiveRentalByUserId(userId)) {
            logger.warn("У пользователя с id: {} уже есть активная аренда", userId);
            throw new BusinessLogicException("Rental for user with id: " + userId + " is already active.");
        }

        if (startPoint.getScooters() != null && !startPoint.getScooters().contains(scooter)) {
            logger.warn("Самокат с id: {} не в точке аренда с id: {}.", scooter.getId(), startPoint.getId());
            throw new BusinessLogicException(
                    "Scooter with id: " + scooter.getId() + " is not at the rental point with id: " + startPoint.getId()
            );
        }
    }

    private Long createRental(User user, Scooter scooter, Tariff tariff, LocationNode startPoint) {
        Rental rental = new Rental(
                null,
                user,
                scooter,
                tariff,
                RentalStatus.ACTIVE,
                null,
                LocalDateTime.now(),
                null,
                startPoint,
                null
        );
        return this.rentalRepository.save(rental).getId();
    }
}
