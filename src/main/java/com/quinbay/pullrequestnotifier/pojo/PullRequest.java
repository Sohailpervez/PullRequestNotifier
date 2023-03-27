package com.quinbay.pullrequestnotifier.pojo;

import lombok.Data;

@Data
public class PullRequest {


    private Long id;
    private String title;
    private Account author;
    private String created_on;
    private Links links;
}
