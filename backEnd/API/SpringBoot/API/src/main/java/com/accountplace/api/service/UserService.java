package com.accountplace.api.service;

import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.dto.crud.pub.UserDto;
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
public class UserService {

    private final UserRepository userRepository;
    private final GroupService groupService;

    /**
     * Constructor injection for UserService dependencies.
     *
     * @param userRepository The repository for user-related database operations.
     * @param groupService The service for handling group-related operations.
     */
    @Autowired
    public UserService(UserRepository userRepository, GroupService groupService) {
        this.userRepository = userRepository;
        this.groupService = groupService;
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
    public List<UserDto> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Retrieves a user entity by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The UserEntity representing the user.
     * @throws EntityNotFoundException if the user cannot be found.
     */
    public UserEntity getEntity(Integer id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Finds a user by its ID and returns it as a DTO.
     *
     * @param Id The ID of the user to retrieve.
     * @return The UserDto representing the user.
     * @throws RuntimeException if the user cannot be found.
     */
    public UserDto findById(Integer Id) {
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
    public UserDto findByEmail(String email) {
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
    public UserDto findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found with username " + username));
        return convertToDto(userEntity);
    }

    /**
     * Creates a new user and saves it to the repository.
     *
     * @param userEntity The user entity to be created.
     * @return The saved user entity.
     */
    public UserEntity create(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    /**
     * Updates an existing user account by its ID with the provided user data.
     *
     * @param id The ID of the user to update.
     * @param account The user entity containing updated data.
     * @return The updated user entity.
     * @throws RuntimeException if the user cannot be found.
     */
    public UserEntity updateAccount(Integer id, UserEntity account) {
        return userRepository.findById(id).map(account1 -> {
            account1.setUsername(account.getUsername());
            account1.setPassword(account.getPassword());
            account1.setEmail(account.getEmail());
            account1.setRoles(account.getRoles());
            return userRepository.save(account1);
        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
    }

    /**
     * Deletes a user account by its ID.
     *
     * @param id The ID of the user to delete.
     * @return A success message indicating the user was deleted.
     */
    public String deleteAccountById(Integer id) {
        userRepository.deleteById(id);
        return "Account with id " + id + " has been deleted successfully";
    }

    /**
     * Converts a UserEntity to a UserDto.
     *
     * @param userEntity The UserEntity to convert.
     * @return The UserDto representing the user.
     */
    private UserDto convertToDto(UserEntity userEntity) {
        Email email =  new Email(userEntity.getEmail());
        List<GroupDto> groups = new ArrayList<>();
        for (GroupEntity grp: userEntity.getGroups()) {
            groups.add(groupService.findById(grp.getId()));
        }
        return new UserDto(
                userEntity.getId(),
                userEntity.getUsername(),
                email,
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getRoles(),
                groups
        );
    }

}
