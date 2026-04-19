package com.accountplace.api.service.crud;

import com.accountplace.api.dto.crud.create.GroupCreateDTO;
import com.accountplace.api.dto.crud.update.GroupUpdateDTO;
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
public class GroupCrudService {

    /**
     * The group Repository
     */
    private final GroupRepository groupRepository;

    /**
     * Constructor injection for GroupService dependencies.
     *
     * @param groupRepository The repository for group-related database operations.
     */
    @Autowired
    private GroupCrudService (GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    /**
     * Creates a new group and saves it to the repository.
     *
     * @param groupCreateDTO The group entity to be created.
     * @return The publicDTO (without sensible data) of the created Entity.
     */
    public PublicGroupDTO create(GroupCreateDTO groupCreateDTO) {
        GroupEntity groupEntity = this.convertCreateDTOToEntity(groupCreateDTO);
        return this.convertEntityToPublicDTO(groupRepository.save(groupEntity));
    }

    /**
     * Updates an existing group by its ID with the provided group data.
     *
     * @param groupUpdateDTO The group entity containing updated data.
     * @return The updated group entity.
     * @throws RuntimeException if the group cannot be found.
     */
    public PublicGroupDTO update(GroupUpdateDTO groupUpdateDTO) {
        return groupRepository.findById(groupUpdateDTO.getId())
            .map(group ->
                {
                    group.setUID(groupUpdateDTO.getUID());
                    group.setName(groupUpdateDTO.getName());
                    group.setPassword(groupUpdateDTO.getPassword());
                    group.setGroup_description(groupUpdateDTO.getDescription());
                    return this.convertEntityToPublicDTO(groupRepository.save(group));
                })
            .orElseThrow(() -> new RuntimeException("Group with " + groupUpdateDTO.getId() + " not found"));

    }

    /**
     * Deletes a group by its ID.
     *
     * @param id The ID of the group to delete.
     * @return A success message indicating the group was deleted.
     */
    public String delete(int id) {
        groupRepository.deleteById(id);
        return "Group with " + id + " has been deleted successfully";
    }


    public GroupEntity convertCreateDTOToEntity(GroupCreateDTO groupCreateDTO) {
        return new GroupEntity(
                groupCreateDTO.getUID(),
                groupCreateDTO.getName(),
                groupCreateDTO.getPassword(),
                groupCreateDTO.getDescription()
        );
    }

    /**
     * Converts a GroupEntity to a GroupDto.
     *
     * @param groupEntity The GroupEntity to convert.
     * @return The GroupDto representing the group.
     */
    private PublicGroupDTO convertEntityToPublicDTO(GroupEntity groupEntity) {
        return new PublicGroupDTO(
                groupEntity.getId(),
                groupEntity.getUID(),
                groupEntity.getName(),
                groupEntity.getGroup_description()
        );
    }
}
