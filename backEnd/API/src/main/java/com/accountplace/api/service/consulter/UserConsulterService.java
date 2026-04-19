package com.accountplace.api.service.consulter;

import com.accountplace.api.dto.crud.pub.PublicRoleDTO;
import com.accountplace.api.dto.crud.pub.PublicUserDTO;
import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.entity.RoleEntity;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.tools.Email;
import com.accountplace.api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to manage user-related operations.
 * It provides methods for creating, updating, deleting, and retrieving user entities.
 */
@Service
public class UserConsulterService {

    private final UserRepository userRepository;
    private final GroupConsulterService groupConsulterService;

    /**
     * Constructor injection for UserService dependencies.
     *
     * @param userRepository The repository for user-related database operations.
     * @param groupConsulterService The service for handling group-related operations.
     */
    @Autowired
    public UserConsulterService(UserRepository userRepository, GroupConsulterService groupConsulterService) {
        this.userRepository = userRepository;
        this.groupConsulterService = groupConsulterService;
    }

    /**
     * Counts the total number of accounts in the system.
     *
     * @return The total number of user accounts.
     */
    public Long countAccount() {
        return userRepository.count();
    }

    /**
     * Retrieves all users and converts them to DTO format.
     *
     * @return A list of UserDto objects representing all users.
     */
    public List<PublicUserDTO> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Finds a user by its ID and returns it as a DTO.
     *
     * @param Id The ID of the user to retrieve.
     * @return The UserDto representing the user.
     * @throws RuntimeException if the user cannot be found.
     */
    public PublicUserDTO findById(Integer Id) {
        UserEntity userEntity = userRepository.findById(Id).orElseThrow(() -> new RuntimeException("user not found with id " + Id));
        return convertToDto(userEntity);
    }

    /**
     * Finds a user by their email and returns it as a DTO.
     *
     * @param email The email of the user to retrieve.
     * @return The UserDto representing the user.
     * @throws EntityNotFoundException if the user cannot be found.
     */
    public PublicUserDTO findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return convertToDto(userEntity);
    }

    /**
     * Finds a user by their username and returns it as a DTO.
     *
     * @param username The username of the user to retrieve.
     * @return The UserDto representing the user.
     * @throws RuntimeException if the user cannot be found.
     */
    public PublicUserDTO findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found with username " + username));
        return convertToDto(userEntity);
    }

    /**
     * Converts a UserEntity to a UserDto.
     *
     * @param userEntity The UserEntity to convert.
     * @return The UserDto representing the user.
     */
    private PublicUserDTO convertToDto(UserEntity userEntity) {
        Email email =  new Email(userEntity.getEmail());
        List<PublicGroupDTO> groups = new ArrayList<>();
        List<PublicRoleDTO> roles = new ArrayList<>();
        for (GroupEntity group: userEntity.getGroups()) {
            groups.add(new PublicGroupDTO(group.getId(), group.getUID(), group.getName(), group.getGroup_description()));
        }
        for (RoleEntity role: userEntity.getRoleEntities()) {
            roles.add(new PublicRoleDTO(role.getId(), role.getName()));
        }
        return new PublicUserDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                email,
                userEntity.getFirstname(),
                userEntity.getLastname(),
                roles,
                groups
        );
    }

}
