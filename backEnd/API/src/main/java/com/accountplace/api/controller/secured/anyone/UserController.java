package com.accountplace.api.controller.secured.anyone;

import com.accountplace.api.dto.crud.pub.PublicUserDTO;
import com.accountplace.api.security.JWTProvider;
import com.accountplace.api.service.consulter.UserConsulterService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/data/user")
@NoArgsConstructor
public class UserController {

    @Autowired
    private UserConsulterService userConsulterService;

    @Autowired
    private JWTProvider jwtProvider;

    @GetMapping("/infos")
    public String info() {
        return "RestController for User Account of this application";
    }

    @GetMapping("/userInfo")
    public ResponseEntity<PublicUserDTO> getPersonalUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtProvider.getIdentifierFromJWT(token);
            PublicUserDTO userDTO= userConsulterService.findByEmail(email);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("count")
    public ResponseEntity<Long> getUserCount() {
        try {
            return ResponseEntity.ok(userConsulterService.countAccount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}


