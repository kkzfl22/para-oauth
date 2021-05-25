package com.paraview.oauth.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Token implements Serializable {

    private static final long serialVersionUID = 2L;

    private String access_token = UUID.randomUUID().toString();

    private String token_type;

    private long expires_in;

    private long timestap;

    public Token(String token_type, long expires_in) {
        this.token_type = token_type;
        this.expires_in = expires_in;
        this.timestap = System.currentTimeMillis();
    }

}
