package com.accountplace.api.service;

import com.accountplace.api.entity.PlatformEntity;
import com.accountplace.api.repositories.PlateformRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage platform-related operations.
 * It provides methods for creating, updating, deleting, and retrieving platform entities.
 */
@Service
public class PlateformService {

    private final PlateformRepository platformRepository;

    /**
     * Constructor injection for PlateformService dependencies.
     *
     * @param platformRepository The repository for platform-related database operations.
     */
    @Autowired
    private PlateformService (PlateformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    /**
     * Retrieves a platform entity by its ID.
     *
     * @param id The ID of the platform to retrieve.
     * @return The PlatformEntity representing the platform.
     * @throws EntityNotFoundException if the platform cannot be found.
     */
    public PlatformEntity getEntity(Integer id) {
        return platformRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Finds a platform by its ID and returns it as a DTO.
     *
     * @param id The ID of the platform to retrieve.
     * @return The PlatformDto representing the platform.
     * @throws EntityNotFoundException if the platform cannot be found.
     */
    public PlatformDto findById(Integer id) {
        PlatformEntity platformEntity = platformRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return this.convertToDto(platformEntity);
    }

    /**
     * Retrieves all platforms and converts them to DTO format.
     *
     * @return A list of PlatformDto objects representing all platforms.
     */
    public List<PlatformDto> listAll() {
        List<PlatformEntity> platformEntities = platformRepository.findAll();
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Searches for platforms based on the platform name and returns them as DTOs.
     *
     * @param name The name of the platform to search for.
     * @return A list of PlatformDto objects associated with the given name.
     */
    public List<PlatformDto> searchByName(String name) {
        List<PlatformEntity> platformEntities = platformRepository.findBySearch(name);
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Creates a new platform and saves it to the repository.
     *
     * @param platformEntity The platform entity to be created.
     * @return The saved platform entity.
     */
    public PlatformEntity create(PlatformEntity platformEntity) {
        return platformRepository.save(platformEntity);
    }

    /**
     * Updates an existing platform by its ID with the provided platform data.
     *
     * @param id The ID of the platform to update.
     * @param platformEntity The platform entity containing updated data.
     * @return The updated platform entity.
     * @throws RuntimeException if the platform cannot be found.
     */
    public PlatformEntity update(Integer id, PlatformEntity platformEntity) {
        return platformRepository.findById(id).map(plateform1 -> {
            plateform1.setName(platformEntity.getName());
            plateform1.setUrl(platformEntity.getUrl());
            plateform1.setImgRef(platformEntity.getImgRef());
            return platformRepository.save(plateform1);
        }).orElseThrow(() -> new RuntimeException("Plateform with " + id + " not found"));
    }

    /**
     * Deletes a platform by its ID.
     *
     * @param id The ID of the platform to delete.
     * @return A success message indicating the platform was deleted.
     */
    public String delete(Integer id) {
        platformRepository.deleteById(id);
        return "Plateform with id " + id + " has been deleted successfully";
    }

    /**
     * Converts a PlatformEntity to a PlatformDto.
     *
     * @param platformEntity The PlatformEntity to convert.
     * @return The PlatformDto representing the platform.
     */
    private PlatformDto convertToDto(PlatformEntity platformEntity) {
        return new PlatformDto(
                platformEntity.getId(),
                platformEntity.getName(),
                platformEntity.getUrl(),
                platformEntity.getImgRef()
        );
    }
}
