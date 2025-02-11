package com.accountplace.api.controller.secured.consulter;

import com.accountplace.api.controller.open.AuthController;
import com.accountplace.api.dto.crud.pub.PublicUserDTO;
import com.accountplace.api.dto.requestBody.register.RegisterUserBodyDTO;
import com.accountplace.api.service.consulter.UserConsulterService;
import com.accountplace.api.service.crud.UserCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/data/user")
public class UserController {

    private final UserConsulterService userConsulterService;
    private final UserCrudService userCrudService;
    private final AuthController authController;

    @Autowired
    private UserController(UserConsulterService userConsulterService, AuthController authController, UserCrudService userCrudService) {
        this.userConsulterService = userConsulterService;
        this.authController = authController;
        this.userCrudService = userCrudService;
    }

    @GetMapping("/infos")
    public String info() {
        return "RestController for User Account of this application";
    }

    @GetMapping("/all")
    public ResponseEntity<List<PublicUserDTO>> getAllUser() {
        try {
            return  ResponseEntity.ok().body(userConsulterService.findAll());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<PublicUserDTO> getUserDtoUsername(@PathVariable("filter") String filter, @PathVariable("value") String search_query) {
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

    @GetMapping("count")
    public ResponseEntity<Long> getUserCount() {
        try {
            return ResponseEntity.ok(userConsulterService.countAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Integer id) {
        try {
            String result =  userCrudService.delete(id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


