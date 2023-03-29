package com.gdn.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Fact {

    @Builder.Default
    private String name = "-";
    private String value;
}
