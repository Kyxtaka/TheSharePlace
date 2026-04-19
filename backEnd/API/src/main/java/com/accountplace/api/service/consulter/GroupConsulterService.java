package com.accountplace.api.service.consulter;

import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.repositories.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage group-related operations.
 * It provides methods for creating, updating, deleting, and retrieving group entities.
 */
@Service
public class GroupConsulterService {

    private final GroupRepository groupRepository;

    /**
     * Constructor injection for GroupService dependencies.
     *
     * @param groupRepository The repository for group-related database operations.
     */
    @Autowired
    private GroupConsulterService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Finds a group by its ID and returns it as a DTO.
     *
     * @param id The ID of the group to retrieve.
     * @return The GroupDto representing the group.
     * @throws EntityNotFoundException if the group cannot be found.
     */
    public PublicGroupDTO findById(Integer id) {
        return this.convertToDto(groupRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Retrieves all groups and converts them to DTO format.
     *
     * @return A list of GroupDto objects representing all groups.
     */
    public List<PublicGroupDTO> findAll() {
        List<GroupEntity> groupEntities = groupRepository.findAll();
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Searches for groups based on the group name and returns them as DTOs.
     *
     * @param name The name of the group to search for.
     * @return A list of GroupDto objects associated with the given name.
     */
    public List<PublicGroupDTO> searchByName(String name) {
        List<GroupEntity> groupEntities = groupRepository.findByName(name);
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    /**
     * Converts a GroupEntity to a GroupDto.
     *
     * @param groupEntity The GroupEntity to convert.
     * @return The GroupDto representing the group.
     */
    private PublicGroupDTO convertToDto(GroupEntity groupEntity) {
        return new PublicGroupDTO(
                groupEntity.getId(),
                groupEntity.getUID(),
                groupEntity.getName(),
                groupEntity.getGroup_description()
        );
    }
}
