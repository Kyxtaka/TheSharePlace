package com.accountplace.api.exceptions.auth;

import lombok.Getter;

@Getter
public class UserAlreadyExistException extends Exception {
    private String type;
    private final String message;
    public UserAlreadyExistException(String type) {
        super("User " + type + " already exists");
        this.type = type;
        this.message = "User " + type + " already exists";
    }
}
