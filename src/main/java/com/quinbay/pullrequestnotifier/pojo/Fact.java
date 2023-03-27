package com.quinbay.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fact {
    private String name;
    private String value;
}
