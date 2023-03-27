package com.quinbay.pullrequestnotifier.pojo;

import lombok.Data;
import java.util.List;

@Data
public class ReposResponse {

    private List<Repository> values;
    private String next;
}
