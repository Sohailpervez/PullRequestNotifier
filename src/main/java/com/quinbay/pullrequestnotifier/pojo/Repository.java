package com.quinbay.pullrequestnotifier.pojo;

import lombok.Data;

@Data
public class Repository {

    private String slug;
    private String name;
    private Links links;
}
