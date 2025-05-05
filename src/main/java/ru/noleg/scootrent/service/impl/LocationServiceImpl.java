package ru.noleg.scootrent.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.scootrent.entity.location.LocationNode;
import ru.noleg.scootrent.exception.NotFoundException;
import ru.noleg.scootrent.exception.ServiceException;
import ru.noleg.scootrent.repository.LocationRepository;
import ru.noleg.scootrent.service.LocationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    @Transactional
    public Long add(LocationNode locationNode) {
        try {

            return this.locationRepository.save(locationNode).getId();
        } catch (Exception e) {

            throw new ServiceException("Error on add location.", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {

            if (!this.locationRepository.existsById(id)) {
                throw new NotFoundException("Location with id: " + id + " not found.");
            }

            this.locationRepository.delete(id);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on delete location.", e);
        }
    }

    @Override
    public LocationNode getLocationById(Long id) {
        try {

            return this.locationRepository.findLocationById(id).orElseThrow(
                    () -> new NotFoundException("Location with id " + id + " not found")
            );
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error on get location.", e);
        }
    }

    @Override
    // TODO возможно добавить кеширование
    public LocationNode getLocationByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        return this.locationRepository.findLocationByCoordinates(latitude, longitude).orElseThrow(
                () -> new NotFoundException("Rental point with latitude: " + latitude + " and longitude: " + longitude + " not found.")
        );
    }

    @Override
    public List<LocationNode> getAllLocations() {
        try {
            List<LocationNode> allNodes = this.locationRepository.findAll();

            Map<Long, LocationNode> idMap = allNodes.stream()
                    .peek(node -> node.getChildren().clear())
                    .collect(Collectors.toMap(LocationNode::getId, Function.identity()));

            List<LocationNode> roots = new ArrayList<>();
            for (LocationNode node : allNodes) {
                LocationNode parent = node.getParent();
                if (parent != null && parent.getId() != null) {
                    idMap.get(parent.getId()).getChildren().add(node);
                } else {
                    roots.add(node);
                }
            }
            return roots;
        } catch (Exception e) {
            throw new ServiceException("Error on get all locations.", e);
        }
    }

    @Override
    public List<LocationNode> getChildrenLocation(Long parentId) {
        return this.locationRepository.findChildrenLocationByParentId(parentId);
    }
}
