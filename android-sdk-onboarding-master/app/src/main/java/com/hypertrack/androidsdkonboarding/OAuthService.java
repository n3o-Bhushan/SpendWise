package com.hypertrack.androidsdkonboarding;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface OAuthService {

    @POST("clientCredentials/oauth2/token/us/gcb")
    Call<OAuthResponse> makeOAuthCall(@Body OAuthRequestBody body, @HeaderMap Map<String, String> headers);
}
