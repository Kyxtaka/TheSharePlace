package com.accountplace.api.controller.consulter;

import com.accountplace.api.controller.AuthController;
import com.accountplace.api.dto.register.RegisterUserBodyDTO;
import com.accountplace.api.dto.review.UserDto;
import com.accountplace.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/api/data/user")
public class UserController {

    private final UserService userService;
    private final AuthController authController;

    @Autowired
    private UserController(UserService userService,AuthController authController) {
        this.userService = userService;
        this.authController = authController;
    }

    @GetMapping("/infos")
    public String info() {
        return "RestController for User Account of this application";
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUser() {
        try {
            return  ResponseEntity.ok().body(userService.findAll());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/find/{filter}/{value}")
    public ResponseEntity<UserDto> getUserDtoUsername(@PathVariable("filter") String filter, @PathVariable("value") String search_query) {
        try {
            switch (filter) {
                case "id" -> {
                    Integer id = Integer.valueOf(search_query);
                    return ResponseEntity.ok(userService.findById(id));
                }
                case "username" -> {
                    return ResponseEntity.ok(userService.findByUsername(search_query));
                }
                case "email" -> {
                    return ResponseEntity.ok(userService.findByEmail(search_query));
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
            return ResponseEntity.ok(userService.countAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("create")
    public ResponseEntity<Map<String, String>> createAccount(@RequestBody RegisterUserBodyDTO bodyDTO) {
        return authController.registerUser(bodyDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") Integer id) {
        try {
            String result =  userService.deleteAccountById(id);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PutMapping("/update")
//    public UserEntity updateUser(@RequestParam("id") int id, @RequestBody  userEntity) {
//        UserEntity response = null;
//        try {
//            response = userService.updateAccount(id, userEntity);
//        } catch (Exception e) {
//            ResponseEntity.notFound().build();
//        }
//        return response;
//    }

}


