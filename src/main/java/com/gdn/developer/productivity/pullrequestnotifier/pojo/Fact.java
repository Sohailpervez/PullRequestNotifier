package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Fact {

    @Builder.Default
    private String name = Constants.HYPHEN;
    private String value;
}
