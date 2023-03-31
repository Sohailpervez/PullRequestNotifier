package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseBody<T> {

    private Boolean success;
    private T data;
    private Error error;
}
