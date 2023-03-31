package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody<T> {

    private Boolean success;
    private T data;
    private Error error;
}
