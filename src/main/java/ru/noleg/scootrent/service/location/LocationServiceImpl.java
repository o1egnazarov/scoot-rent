package ru.noleg.scootrent.service.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.entity.location.LocationType;
import ru.noleg.scootrent.exception.BusinessLogicException;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Long add(LocationNode locationNode) {
        logger.debug("Adding new location: {}.", locationNode.getTitle());

        this.validateRentalPoint(locationNode);
        LocationNode savedLocation = this.locationRepository.save(locationNode);

        logger.debug("Location successfully added. Id: {}, Title: {}.", savedLocation.getId(), savedLocation.getTitle());
        return savedLocation.getId();
    }

    @Override
    public LocationNode update(Long id, UpdateLocationCommand updateLocationCommand) {
        logger.debug("Updating location with: {}.", id);

        LocationNode locationNode = this.getLocationById(id);

        this.mapToLocationFromUpdateCommand(updateLocationCommand, locationNode);

        LocationNode updatedLocationNode = this.locationRepository.save(locationNode);
        logger.debug("Location with id: {} successfully updated.", id);
        return updatedLocationNode;
    }

    private void validateRentalPoint(LocationNode locationNode) {
        if (locationNode.getParent() != null && !this.locationRepository.existsById(locationNode.getParent().getId())) {
            logger.error("Parent location with id: {} not found", locationNode.getParent().getId());
            throw new NotFoundException("Parent location with id " + locationNode.getParent().getId() + " does not exist.");
        }
    }

    private void mapToLocationFromUpdateCommand(UpdateLocationCommand updateLocationCommand, LocationNode locationNode) {
        locationNode.setTitle(updateLocationCommand.title());
        locationNode.setAddress(updateLocationCommand.address());
        locationNode.setLocationType(updateLocationCommand.locationType());
        locationNode.setLatitude(updateLocationCommand.latitude());
        locationNode.setLongitude(updateLocationCommand.longitude());
        this.updateParent(locationNode, updateLocationCommand.parentId());
    }

    private void updateParent(LocationNode locationNode, Long newParentId) {
        if (newParentId == null) {
            locationNode.setParent(null);
            return;
        }

        if (newParentId.equals(locationNode.getId())) {
            logger.warn("Location: {} cannot be its parent.", locationNode.getTitle());
            throw new BusinessLogicException("Location cannot be its parent.");
        }

        LocationNode parent = this.locationRepository.findById(newParentId).orElseThrow(() -> {
                    logger.error("Parent location with id: {} not found.", newParentId);
                    return new NotFoundException("Parent location with id " + newParentId + " not found.");
                }
        );

        locationNode.setParent(parent);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting location with id: {}.", id);

        if (!this.locationRepository.existsById(id)) {
            logger.error("Location with id: {} does not exist.", id);
            throw new NotFoundException("Location with id: " + id + " not found.");
        }
        this.locationRepository.delete(id);

        logger.debug("Location with id: {} successfully deleted.", id);
    }

    @Override
    public LocationNode getLocationById(Long id) {
        logger.debug("Fetching location by id: {}.", id);

        return this.locationRepository.findLocationById(id).orElseThrow(
                () -> {
                    logger.error("Location with id: {} not found.", id);
                    return new NotFoundException("Location with id: " + id + " not found.");
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LocationNode getLocationByIdAndType(Long id, LocationType type) {
        logger.debug("Fetching location by id: {} and type: {}.", id, type);

        return this.locationRepository.findLocationByIdAndType(id, type).orElseThrow(
                () -> {
                    logger.error("Location with id:{} and type: {} - not found.", id, type);
                    return new NotFoundException("Location with id: " + id + " and type: " + type + " - not found.");
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public LocationNode getLocationByCoordinatesAndType(BigDecimal latitude, BigDecimal longitude, LocationType type) {
        logger.debug("Searching location by coordinates: {}, {} and type: {}.", latitude, longitude, type);

        return this.locationRepository.findLocationByCoordinatesAndType(latitude, longitude, type).orElseThrow(
                () -> {
                    logger.error("Location with coordinates: {}, {} and type: {} not found.", latitude, longitude, type);
                    return new NotFoundException("Location with latitude: " + latitude +
                            " and longitude: " + longitude + " and type: " + type + " not found."
                    );
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationNode> getChildrenLocation(Long parentId) {
        logger.debug("Fetching child locations for parent id: {}.", parentId);

        List<LocationNode> children = this.locationRepository.findChildrenLocationByParentId(parentId);

        logger.info("Found {} child locations for parent id: {}.", children.size(), parentId);
        return children;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationNode> getAllLocations() {
        try {

            List<LocationNode> allNodes = this.locationRepository.findAll();
            logger.debug("Found {} locations.", allNodes.size());

            Map<Long, LocationNode> idMap = allNodes.stream()
                    .peek(node -> {
                        if (node.getChildren() != null) {
                            logger.trace("Clearing children for location id: {}.", node.getId());
                            node.getChildren().clear();
                        }
                    })
                    .collect(Collectors.toMap(LocationNode::getId, Function.identity()));

            List<LocationNode> roots = new ArrayList<>();
            for (LocationNode node : allNodes) {

                LocationNode parent = node.getParent();
                if (parent != null && parent.getId() != null) {

                    idMap.get(parent.getId()).getChildren().add(node);
                    logger.trace("Attached node id={} to parent id={}.", node.getId(), parent.getId());
                } else {

                    roots.add(node);
                }
            }

            logger.info("Successfully built location hierarchy. Root count: {}.", roots.size());
            return roots;
        } catch (Exception e) {
            logger.error("Failed to get all locations.", e);
            throw new ServiceException("Error on get all locations.", e);
        }
    }
}
