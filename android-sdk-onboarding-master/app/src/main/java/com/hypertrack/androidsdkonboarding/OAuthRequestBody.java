package com.hypertrack.androidsdkonboarding;

import com.google.gson.annotations.SerializedName;

public class OAuthRequestBody {

    @SerializedName("grant_type")
    private String grant_type;
    @SerializedName("scope")
    private String scope;

    public OAuthRequestBody(String grant_type, String scope) {
        this.grant_type = grant_type;
        this.scope = scope;
    }


}
