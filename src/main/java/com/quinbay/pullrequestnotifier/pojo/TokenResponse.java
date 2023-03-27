package com.quinbay.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponse {
    private String access_token;
}
