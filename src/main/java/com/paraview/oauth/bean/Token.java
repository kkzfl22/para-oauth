package com.paraview.oauth.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Token {

    private String access_token = UUID.randomUUID().toString();

    private String token_type;

    private long expires_in;

    public Token(String token_type, long expires_in) {
        this.token_type = token_type;
        this.expires_in = expires_in;
    }
}
