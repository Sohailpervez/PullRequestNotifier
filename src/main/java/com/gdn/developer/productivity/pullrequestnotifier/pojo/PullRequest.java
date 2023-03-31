package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullRequest {

    private Long id;
    private String title;
    private Account author;
    private String created_on;
    private Links links;
}
