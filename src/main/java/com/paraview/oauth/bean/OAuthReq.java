package com.paraview.oauth.bean;

import com.paraview.oauth.enums.AuthorizeType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OAuthReq {

    private String username;
    private String password;
    private String grant_type = AuthorizeType.PASSWORD.value();
    private String response_type;
    private String client_id;
    private String state;
    private String redirect_uri;
    private String scope;
    private String authorization;

}
