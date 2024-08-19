package com.accountplace.api.dto.review;

import com.accountplace.api.tools.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class AccountDTO {
    private Integer id;
    private String username;
    private String password;
    private Email email;
    private boolean a2f;
    private GroupDto group;
    private PlatformDto platform;
}
