package com.accountplace.api.tools;

import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an Email object with validation functionality.
 * Provides methods to store an email address and validate its format.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class Email {
    private String mailAddress;

    /**
     * Validates the email address format.
     * This method checks if the email address matches the general pattern of an email.
     * The pattern used is a simple regular expression for email validation.
     *
     * @return true if the email address is valid, false otherwise.
     */
    public boolean isValid() {
        // Regular expression for basic email validation
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(this.mailAddress);
        return m.matches();
    }
}
