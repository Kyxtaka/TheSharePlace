package com.accountplace.api.dto.review;

import com.accountplace.api.entity.Role;
import com.accountplace.api.tools.Email;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private Email email;
    private String firstname;
    private String lastname;
    private List<Role> roles;
    private List<GroupDto> groups;
}
