package com.hypertrack.androidsdkonboarding;

import com.google.gson.annotations.SerializedName;


public class OAuthResponse {

    @SerializedName("access_token")
    public String access_token;

    @SerializedName("refresh_token")
    public String refresh_token;

    @SerializedName("scope")
    public String scope;

    @SerializedName("token_type")
    public String token_type;

    @SerializedName("expires_in")
    public float expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public float getExpires_in() {
        return expires_in;
    }
}
