package com.accountplace.api.controller.consulter;

import com.accountplace.api.dto.register.RegisterGroupDTO;
import com.accountplace.api.dto.review.GroupDto;
import com.accountplace.api.entity.GroupEntity;
import com.accountplace.api.repositories.GroupRepository;
import com.accountplace.api.service.GroupService;
import com.accountplace.api.tools.RandomUIDProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data/group")
public class GroupController {

    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    @Autowired
    private GroupController(GroupService groupService, GroupRepository groupRepository, PasswordEncoder passwordEncoder, GroupRepository groupRepository1) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupDto>> getAllGroups() {
        List<GroupDto> result = null;
        try {
            result = groupService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<GroupDto> findById(@PathVariable("filter") String filter, @PathVariable("value") String value) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (value ==  null || value.isEmpty()) { return ResponseEntity.notFound().build();}
        if (filter.equals("id")) {
            Integer id = Integer.valueOf(value);
            GroupDto group = groupService.findById(id);
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search/{filter}")
    public ResponseEntity<List<GroupDto>> findByName(@PathVariable("filter") String filter, @RequestParam("search_query") String search_query) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (search_query ==  null || search_query.isEmpty()) { return ResponseEntity.notFound().build();}
        List<GroupDto> groups = null;
        try {
            switch (filter) {
                case "name" -> {
                    Integer id = Integer.valueOf(search_query);
                    groups = groupService.searchByName(search_query);
                    return ResponseEntity.ok(groups);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping("/create")
    public ResponseEntity<GroupDto> createGroup(@RequestBody RegisterGroupDTO bodyDTO) {
        try {
            GroupEntity group = new GroupEntity();
            Long generatedUID =  1000000001L;
            while (this.groupRepository.existsByUID(generatedUID)) {
                generatedUID = RandomUIDProvider.generateRandom10DigitNumber();
            }
            group.setName(bodyDTO.getGroupName());
            group.setGroup_description(bodyDTO.getGroupDescription());
            group.setPassword(passwordEncoder.encode(bodyDTO.getGroupPassword()));
            group.setUID(generatedUID);
            GroupEntity result = groupService.create(group);
            return ResponseEntity.ok(groupService.findById(result.getId()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable("id") Integer id, @RequestBody RegisterGroupDTO bodyDTO) {
        try {
            GroupEntity group = groupService.getGroupEntity(id);
            group.setName(bodyDTO.getGroupName());
            group.setGroup_description(bodyDTO.getGroupDescription());
            group.setPassword(passwordEncoder.encode(bodyDTO.getGroupPassword()));
            GroupEntity result = groupService.updateById(id, group);
            return ResponseEntity.ok(groupService.findById(result.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteGroup(@PathVariable("id") Integer id) {
        try {
            String result = groupService.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
