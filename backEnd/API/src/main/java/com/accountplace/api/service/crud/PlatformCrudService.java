package com.accountplace.api.service.crud;

import com.accountplace.api.dto.crud.create.PlatformCreateDTO;
import com.accountplace.api.dto.crud.update.PlatformUpdateDTO;
import com.accountplace.api.dto.crud.update.PublicPlatformDTO;
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
public class PlatformCrudService {

    private final PlateformRepository platformRepository;

    /**
     * Constructor injection for PlateformService dependencies.
     *
     * @param platformRepository The repository for platform-related database operations.
     */
    @Autowired
    private PlatformCrudService (PlateformRepository platformRepository) {
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
     * Creates a new platform and saves it to the repository.
     *
     * @param createDTO The platform entity to be created.
     * @return The saved platform entity.
     */
    public PublicPlatformDTO create(PlatformCreateDTO createDTO) {
        PlatformEntity platformEntity = this.convertCreateToEntity(createDTO);
        return this.convertEntityToPublicDTO(platformRepository.save(platformEntity));
    }

    /**
     * Updates an existing platform by its ID with the provided platform data.
     *
     * @param platformUpdateDTO The platform DTO containing updated data.
     * @return The updated platform entity.
     * @throws RuntimeException if the platform cannot be found.
     */
    public PublicPlatformDTO update(PlatformUpdateDTO platformUpdateDTO) {
        return platformRepository.findById(platformUpdateDTO.getPlateformId())
                .map(platform -> {
                    platform.setName(platformUpdateDTO.getPlateformName());
                    platform.setUrl(platformUpdateDTO.getUrl());
                    platform.setImgRef(platformUpdateDTO.getImgRef());
                    return this.convertEntityToPublicDTO(platformRepository.save(platform));
                })
                .orElseThrow(() -> new RuntimeException("Plateform with " + platformUpdateDTO.getPlateformId() + " not found"));
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

    public PlatformEntity convertCreateToEntity(PlatformCreateDTO platformCreateDTO) {
        return new PlatformEntity(
          platformCreateDTO.getPlateformName(),
          platformCreateDTO.getUrl(),
          platformCreateDTO.getImgRef()
        );
    }

    /**
     * Converts a PlatformEntity to a PlatformDto.
     *
     * @param platformEntity The PlatformEntity to convert.
     * @return The PlatformDto representing the platform.
     */
    private PublicPlatformDTO convertEntityToPublicDTO(PlatformEntity platformEntity) {
        return new PublicPlatformDTO(
                platformEntity.getId(),
                platformEntity.getName(),
                platformEntity.getUrl(),
                platformEntity.getImgRef()
        );
    }
}
