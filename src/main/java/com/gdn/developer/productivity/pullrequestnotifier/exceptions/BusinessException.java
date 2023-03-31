package com.gdn.developer.productivity.pullrequestnotifier.exceptions;

import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BusinessException extends Exception {

    private ErrorCode errorCode;
    private String errorMessage;
}
