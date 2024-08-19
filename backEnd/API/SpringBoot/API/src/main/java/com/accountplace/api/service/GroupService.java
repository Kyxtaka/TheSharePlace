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

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    private GroupService (GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public GroupEntity create(GroupEntity groupEntity) {
        return groupRepository.save(groupEntity);
    }

    public GroupEntity getGroupEntity(Integer id) {
        return groupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public GroupDto findById(Integer id) {
        return this.convertToDto(groupRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public List<GroupDto> findAll() {
        List<GroupEntity> groupEntities = groupRepository.findAll();
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<GroupDto> searchByName(String name) {
        List<GroupEntity> groupEntities = groupRepository.findByName(name);
        return groupEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public GroupEntity updateById(Integer id, GroupEntity groupEntity) {
        return groupRepository.findById(id).map( group -> {
            group.setUID(groupEntity.getUID());
            group.setName(groupEntity.getName());
            group.setPassword(groupEntity.getPassword());
            group.setGroup_description(groupEntity.getGroup_description());
            return groupRepository.save(group);
        }).orElseThrow( () -> new RuntimeException("Group with " + id + " not found"));
    }

    public String deleteById(Integer id) {
        groupRepository.deleteById(id);
        return "Group with " + id + " has been deleted successfully";
    }
    public boolean isUIDexist(Long uid) {
        return groupRepository.existsById(Math.toIntExact(uid));
    }

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
