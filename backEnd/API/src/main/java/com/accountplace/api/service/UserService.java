package com.accountplace.api.service;

import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.entity.UserEntity;
import com.accountplace.api.dto.review.UserDto;
import com.accountplace.api.tools.Email;
import com.accountplace.api.dto.review.GroupDto;
import com.accountplace.api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupService groupService;

    @Autowired
    public UserService(UserRepository userRepository, GroupService groupService) {
        this.userRepository = userRepository;
        this.groupService = groupService;
    }
    public Long countAccount() {
        return userRepository.count();
    }

    public List<UserDto> findAll() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public UserEntity getEntity(Integer id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public UserDto findById(Integer Id) {
        UserEntity userEntity = userRepository.findById(Id).orElseThrow(() -> new RuntimeException("user not found with id " + Id));
        return convertToDto(userEntity);
    }

    public UserDto findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        return convertToDto(userEntity);
    }

    public UserDto findByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user not found with username " + username));
        return convertToDto(userEntity);
    }



    public UserEntity create(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity updateAccount(Integer id, UserEntity account) {
        return userRepository.findById(id).map(account1 -> {
            account1.setUsername(account.getUsername());
            account1.setPassword(account.getPassword());
            account1.setEmail(account.getEmail());
            account1.setRoles(account.getRoles());
            return userRepository.save(account1);
        }).orElseThrow(() -> new RuntimeException("Account not found with id " + id));
    }

    public String deleteAccountById(Integer id) {
        userRepository.deleteById(id);
        return "Account with id " + id + " has been deleted successfully";
    }

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
