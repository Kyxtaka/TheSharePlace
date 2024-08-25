package com.accountplace.api.dto.review;

import lombok.*;

@Data
@AllArgsConstructor
public class GroupDto{
    private Integer id;
    private Long UID;
    private String name;
    private String description;
    private String password;
}
