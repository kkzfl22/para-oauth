package com.paraview.oauth.service.invoker;

import com.paraview.oauth.bean.OAuthReq;
import com.paraview.oauth.bean.Token;
import com.paraview.oauth.client.ClientApp;

public interface AuthInvoker {

    String grantType();

    Token doInvoker(OAuthReq req, ClientApp clientApp);

    boolean checkAuth(ClientApp clientApp);

}
