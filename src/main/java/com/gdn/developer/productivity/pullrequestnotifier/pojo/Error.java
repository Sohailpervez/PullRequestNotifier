package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {
    private ErrorCode errorCode;
    private String errorMessage;
}
