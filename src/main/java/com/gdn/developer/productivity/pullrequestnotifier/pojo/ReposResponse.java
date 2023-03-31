package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReposResponse {

    private List<Repository> values;
    private String next;
}
