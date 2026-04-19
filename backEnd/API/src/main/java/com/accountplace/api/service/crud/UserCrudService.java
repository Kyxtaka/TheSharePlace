package com.accountplace.api.service.crud;

import com.accountplace.api.dto.crud.create.UserCreateDTO;
import com.accountplace.api.dto.crud.pub.PublicRoleDTO;
import com.accountplace.api.dto.crud.pub.PublicUserDTO;
import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.entity.RoleEntity;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.exceptions.auth.UserAlreadyExistException;
import com.accountplace.api.repositories.RoleRepository;
import com.accountplace.api.tools.Email;
import com.accountplace.api.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service class to manage user-related operations.
 * It provides methods for creating, updating, deleting, and retrieving user entities.
 */
@Service
public class UserCrudService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * Constructor injection for UserService dependencies.
     *
     * @param userRepository The repository for user-related database operations.
     */
    @Autowired
    public UserCrudService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Creates a new user and saves it to the repository.
     *
     * @param createDTO The user entity to be created.
     * @return The saved user entity.
     */
    @Transactional
    public PublicUserDTO create(UserCreateDTO createDTO) throws UserAlreadyExistException, RuntimeException {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new UserAlreadyExistException("EMAIL");
        } else if (userRepository.existsByUsername(createDTO.getUsername())) {
            throw new UserAlreadyExistException("USERNAME");
        }
        UserEntity createdUser = this.convertCreateToEntity(createDTO);
        this.userRepository.save(createdUser);
        return this.convertEntityToPublicDTO(createdUser);
    }

    /**
     * Updates an existing user account by its ID with the provided user data.
     *
     * @param id The ID of the user to update.
     * @param account The user entity containing updated data.
     * @return The updated user entity.
     * @throws RuntimeException if the user cannot be found.
     */
    public UserEntity update(Integer id, UserEntity account) {
        return userRepository.findById(id).map(account1 -> {
            account1.setUsername(account.getUsername());
            account1.setPassword(account.getPassword());
            account1.setEmail(account.getEmail());
            account1.setRoleEntities(account.getRoleEntities());
            return userRepository.save(account1);
        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
    }

    /**
     * Deletes a user account by its ID.
     *
     * @param id The ID of the user to delete.
     * @return A success message indicating the user was deleted.
     */
    @Transactional
    public String delete(Integer id) throws RuntimeException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("No user with id: " + id + " found");
        }
        this.userRepository.deleteById(id);
        return "Account with id " + id + " has been deleted successfully";
    }

    @Transactional
    public PublicUserDTO grantRoleToUser(int userId, int roleId) throws Exception {
        UserEntity userEntity = this.userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        RoleEntity roleEntity = this.roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        /*
        for (RoleEntity role : userEntity.getRoleEntities()) {
            if (role.getId() == roleId) throw new RuntimeException("User with id:" +userId+ " already has the role id:" +roleId);
        }*/
        if (userEntity.getRoleEntities().contains(roleEntity)) {
            throw new RuntimeException("User with id:" +userId+ " already has the role id:" +roleId);
        }
        userEntity.getRoleEntities().add(roleEntity);
        userRepository.save(userEntity);
        return this.convertEntityToPublicDTO(userEntity);
    }

    @Transactional
    public PublicUserDTO revokeRoleToUser(int userId, int roleId) throws Exception {
        UserEntity userEntity = this.userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        RoleEntity roleEntity = this.roleRepository.findById(roleId).orElseThrow(EntityNotFoundException::new);
        if (!userEntity.getRoleEntities().contains(roleEntity)) {
            throw new RuntimeException("User with id:" +userId+ " dont has the role id:" +roleId);
        }
        userEntity.getRoleEntities().remove(roleEntity);
        userRepository.save(userEntity);
        return this.convertEntityToPublicDTO(userEntity);
    }

    /**
     * Converts a UserEntity to a UserDto.
     *
     * @param userEntity The UserEntity to convert.
     * @return The UserDto representing the user.
     */
    public PublicUserDTO convertEntityToPublicDTO(UserEntity userEntity) {
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

    public UserEntity convertCreateToEntity(UserCreateDTO createDTO) throws EntityNotFoundException {
        UserEntity user = new UserEntity();
        user.setUsername(createDTO.getUsername());
        user.setEmail(createDTO.getEmail());
        user.setFirstname(createDTO.getFirstname());
        user.setLastname(createDTO.getLastname());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        RoleEntity defaultRole = roleRepository.findByName("USER").get();
        if (!(user.getRoleEntities().contains(defaultRole))) user.getRoleEntities().add(defaultRole);
        for (String roleName: createDTO.getRoles()) {
            Optional<RoleEntity> role = roleRepository.findByName(roleName);
            if (role.isPresent() && (!(user.getRoleEntities().contains(role.get())))) {
                System.out.println("Role found: " + role.get().getName() +" with id "+ role.get().getId());
                user.getRoleEntities().add(role.get());

            }
        }
        return user;
    }
}
