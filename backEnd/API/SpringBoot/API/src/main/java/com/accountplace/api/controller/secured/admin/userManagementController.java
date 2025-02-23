package com.accountplace.api.controller.secured.admin;

import com.accountplace.api.dto.crud.pub.PublicUserDTO;
import com.accountplace.api.repositories.RoleRepository;
import com.accountplace.api.service.consulter.UserConsulterService;
import com.accountplace.api.service.crud.UserCrudService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/admin/data/user")
public class userManagementController {

    private final UserCrudService userCrudService;

    @Autowired
    private UserConsulterService userConsulterService;

    @Autowired
    public userManagementController(UserCrudService userCrudService, RoleRepository roleRepository) {
        this.userCrudService = userCrudService;
        //this.roleRepository = roleRepository;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        try {
            this.userCrudService.delete(id);
            return ResponseEntity.ok("Deleted user with id " + id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message","Internal Server Error"));
        }
    }

    @PutMapping("/grantAuthority/{userID}")
    public ResponseEntity<Object> grantAuthority(@PathVariable int userID, @RequestParam("authority") int authority) {
        try {
            PublicUserDTO user = this.userCrudService.grantRoleToUser(userID, authority);
            return ResponseEntity.ok(user);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User or role not found"));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Internal Server Error"));
        }
    }

    @PutMapping("/revokeAuthority/{userID}")
    public ResponseEntity<Object> revokeAuthority(@PathVariable int userID, @RequestParam("authority") int authority) {
        try {
            PublicUserDTO user = this.userCrudService.revokeRoleToUser(userID, authority);
            return ResponseEntity.ok(user);
        }catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "User or role not found"));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Internal Server Error"));
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<PublicUserDTO>> getAll() {
        try {
            return  ResponseEntity.ok().body(userConsulterService.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<PublicUserDTO> getUserByFilter(@PathVariable("filter") String filter, @PathVariable("value") String search_query) {
        try {
            switch (filter) {
                case "id" -> {
                    Integer id = Integer.valueOf(search_query);
                    return ResponseEntity.ok(userConsulterService.findById(id));
                }
                case "username" -> {
                    return ResponseEntity.ok(userConsulterService.findByUsername(search_query));
                }
                case "email" -> {
                    return ResponseEntity.ok(userConsulterService.findByEmail(search_query));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
