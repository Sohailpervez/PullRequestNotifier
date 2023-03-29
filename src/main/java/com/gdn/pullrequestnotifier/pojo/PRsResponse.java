package com.gdn.pullrequestnotifier.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PRsResponse {

    private List<PullRequest> values;
    private String next;
}
