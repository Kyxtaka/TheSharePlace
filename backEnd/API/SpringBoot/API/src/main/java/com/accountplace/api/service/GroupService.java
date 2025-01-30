package com.accountplace.api.service;

import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.dto.review.GroupDto;
import com.accountplace.api.repositories.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage group-related operations.
 * It provides methods for creating, updating, deleting, and retrieving group entities.
 */
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * Constructor injection for GroupService dependencies.
     *
     * @param groupRepository The repository for group-related database operations.
     */
    @Autowired
    private GroupService (GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Creates a new group and saves it to the repository.
     *
     * @param groupEntity The group entity to be created.
     * @return The saved group entity.
     */
    public GroupEntity create(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    /**
     * Retrieves a group entity by its ID.
     *
     * @param id The ID of the group to retrieve.
     * @return The GroupEntity representing the group.
     * @throws EntityNotFoundException if the group cannot be found.
     */
    public GroupEntity getGroupEntity(Integer id) {
        return groupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Finds a group by its ID and returns it as a DTO.
     *
     * @param id The ID of the group to retrieve.
     * @return The GroupDto representing the group.
     * @throws EntityNotFoundException if the group cannot be found.
     */
    public GroupDto findById(Integer id) {
        return this.convertToDto(groupRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    /**
     * Retrieves all groups and converts them to DTO format.
     *
     * @return A list of GroupDto objects representing all groups.
     */
    public List<GroupDto> findAll() {
        List<GroupEntity> groupEntities = groupRepository.findAll();
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Searches for groups based on the group name and returns them as DTOs.
     *
     * @param name The name of the group to search for.
     * @return A list of GroupDto objects associated with the given name.
     */
    public List<GroupDto> searchByName(String name) {
        List<GroupEntity> groupEntities = groupRepository.findByName(name);
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Updates an existing group by its ID with the provided group data.
     *
     * @param id The ID of the group to update.
     * @param groupEntity The group entity containing updated data.
     * @return The updated group entity.
     * @throws RuntimeException if the group cannot be found.
     */
    public GroupEntity updateById(Integer id, GroupEntity groupEntity) {
        return groupRepository.findById(id).map(group -> {
            group.setUID(groupEntity.getUID());
            group.setName(groupEntity.getName());
            group.setPassword(groupEntity.getPassword());
            group.setGroup_description(groupEntity.getGroup_description());
            return groupRepository.save(group);
        }).orElseThrow(() -> new RuntimeException("Group with " + id + " not found"));
    }

    /**
     * Deletes a group by its ID.
     *
     * @param id The ID of the group to delete.
     * @return A success message indicating the group was deleted.
     */
    public String deleteById(Integer id) {
        groupRepository.deleteById(id);
        return "Group with " + id + " has been deleted successfully";
    }

    /**
     * Checks if a group with a given UID exists.
     *
     * @param uid The UID to check for existence.
     * @return true if the group exists, false otherwise.
     */
    public boolean isUIDexist(Long uid) {
        return groupRepository.existsById(Math.toIntExact(uid));
    }

    /**
     * Converts a GroupEntity to a GroupDto.
     *
     * @param groupEntity The GroupEntity to convert.
     * @return The GroupDto representing the group.
     */
    private GroupDto convertToDto(GroupEntity groupEntity) {
        return new GroupDto(
                groupEntity.getId(),
                groupEntity.getUID(),
                groupEntity.getName(),
                groupEntity.getGroup_description(),
                groupEntity.getPassword()
        );
    }
}
