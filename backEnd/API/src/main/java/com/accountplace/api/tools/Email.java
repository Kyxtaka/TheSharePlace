package com.accountplace.api.tools;

import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Email {
    String mailAddress;

    public boolean isValid() {
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(this.mailAddress);
        return m.matches();
    }
}