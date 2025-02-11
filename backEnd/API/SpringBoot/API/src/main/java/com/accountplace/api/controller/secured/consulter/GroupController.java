package com.accountplace.api.controller.secured.consulter;

import com.accountplace.api.dto.crud.update.PublicGroupDTO;
import com.accountplace.api.dto.crud.update.PublicPlatformDTO;
import com.accountplace.api.dto.requestBody.register.RegisterGroupDTO;
import com.accountplace.api.repositories.GroupRepository;
import com.accountplace.api.service.consulter.GroupConsulterService;
import com.accountplace.api.service.crud.GroupCrudService;
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

    private final GroupConsulterService groupConsulterService;
    private final GroupCrudService groupCrudService;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;

    @Autowired
    private GroupController(GroupConsulterService groupConsulterService, GroupCrudService groupCrudService, GroupRepository groupRepository) {
        this.groupConsulterService = groupConsulterService;
        this.groupCrudService = groupCrudService;
        this.groupRepository = groupRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/all")
    public ResponseEntity<List<PublicGroupDTO>> getAllGroups() {
        List<PublicGroupDTO> result = null;
        try {
            result = groupConsulterService.findAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<PublicGroupDTO> findById(@PathVariable("filter") String filter, @PathVariable("value") String value) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (value ==  null || value.isEmpty()) { return ResponseEntity.notFound().build();}
        if (filter.equals("id")) {
            Integer id = Integer.valueOf(value);
            PublicGroupDTO group = groupConsulterService.findById(id);
            return ResponseEntity.ok(group);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search/{filter}")
    public ResponseEntity<List<PublicGroupDTO>> findByName(@PathVariable("filter") String filter, @RequestParam("search_query") String search_query) {
        if (filter == null || filter.isEmpty()) { return ResponseEntity.notFound().build();}
        if (search_query ==  null || search_query.isEmpty()) { return ResponseEntity.notFound().build();}
        List<PublicGroupDTO> groups = null;
        try {
            switch (filter) {
                case "name" -> {
                    Integer id = Integer.valueOf(search_query);
                    groups = groupConsulterService.searchByName(search_query);
                    return ResponseEntity.ok(groups);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteGroup(@PathVariable("id") Integer id) {
        try {
            String result = this.groupCrudService.delete(id);
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


//    @PostMapping("/create")
//    public ResponseEntity<PublicGroupDTO> createGroup(@RequestBody RegisterGroupDTO bodyDTO) {
//        try {
//            GroupEntity group = new GroupEntity();
//            Long generatedUID =  1000000001L;
//            while (this.groupRepository.existsByUID(generatedUID)) {
//                generatedUID = RandomUIDProvider.generateRandom10DigitNumber();
//            }
//            group.setName(bodyDTO.getGroupName());
//            group.setGroup_description(bodyDTO.getGroupDescription());
//            group.setPassword(passwordEncoder.encode(bodyDTO.getGroupPassword()));
//            group.setUID(generatedUID);
//            GroupEntity result = groupCrudService.create(group);
//            return ResponseEntity.ok(groupConsulterService.findById(result.getId()));
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

//    @GetMapping("/update/{id}")
//    public ResponseEntity<GroupDto> updateGroup(@PathVariable("id") Integer id, @RequestBody RegisterGroupDTO bodyDTO) {
//        try {
//            GroupEntity group = groupConsulterService.getById(id);
//            group.setName(bodyDTO.getGroupName());
//            group.setGroup_description(bodyDTO.getGroupDescription());
//            group.setPassword(passwordEncoder.encode(bodyDTO.getGroupPassword()));
//            GroupEntity result = groupConsulterService.updateById(id, group);
//            return ResponseEntity.ok(groupConsulterService.findById(result.getId()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

}
