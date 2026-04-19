package com.accountplace.api.service.consulter;

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
public class PlatformConsulterService {

    private final PlateformRepository platformRepository;

    /**
     * Constructor injection for PlateformService dependencies.
     *
     * @param platformRepository The repository for platform-related database operations.
     */
    @Autowired
    private PlatformConsulterService(PlateformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }
    
    /**
     * Finds a platform by its ID and returns it as a DTO.
     *
     * @param id The ID of the platform to retrieve.
     * @return The PublicPlatformDTO representing the platform.
     * @throws EntityNotFoundException if the platform cannot be found.
     */
    public PublicPlatformDTO findById(Integer id) {
        PlatformEntity platformEntity = platformRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return this.convertToDto(platformEntity);
    }

    /**
     * Retrieves all platforms and converts them to DTO format.
     *
     * @return A list of PublicPlatformDTO objects representing all platforms.
     */
    public List<PublicPlatformDTO> listAll() {
        List<PlatformEntity> platformEntities = platformRepository.findAll();
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Searches for platforms based on the platform name and returns them as DTOs.
     *
     * @param name The name of the platform to search for.
     * @return A list of PublicPlatformDTO objects associated with the given name.
     */
    public List<PublicPlatformDTO> searchByName(String name) {
        List<PlatformEntity> platformEntities = platformRepository.findBySearch(name);
        return platformEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Converts a PlatformEntity to a PublicPlatformDTO.
     *
     * @param platformEntity The PlatformEntity to convert.
     * @return The PublicPlatformDTO representing the platform.
     */
    private PublicPlatformDTO convertToDto(PlatformEntity platformEntity) {
        return new PublicPlatformDTO(
                platformEntity.getId(),
                platformEntity.getName(),
                platformEntity.getUrl(),
                platformEntity.getImgRef()
        );
    }
}
