package com.gdn.developer.productivity.pullrequestnotifier.utils;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHelper {

    public BusinessException checkBusinessException(String errorMessage) {

        if(errorMessage.contains("Invalid OAuth client credentials")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.INVALID_CREDENTIALS)
                    .errorMessage(ErrorMessage.INVALID_CREDENTIALS)
                    .build();
        }
        else if(errorMessage.contains("Token is invalid or not supported for this endpoint")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.UNAUTHORIZED)
                    .errorMessage(ErrorMessage.UNAUTHORIZED)
                    .build();
        }
        else if(errorMessage.contains("UnknownHostException")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.UNKNOWN_HOST)
                    .errorMessage(ErrorMessage.UNKNOWN_HOST)
                    .build();
        }
        else if(errorMessage.contains("Network is unreachable")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.NETWORK_UNREACHABLE)
                    .errorMessage(ErrorMessage.NETWORK_UNREACHABLE)
                    .build();
        }
        else if(errorMessage.contains("Invalid webhook URL")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.INVALID_WEBHOOK_URL)
                    .errorMessage(ErrorMessage.INVALID_WEBHOOK_URL)
                    .build();
        }
        else if(errorMessage.contains("URI is not absolute")) {
            return BusinessException.builder()
                    .errorCode(ErrorCode.URI_NOT_ABSOLUTE)
                    .errorMessage(ErrorMessage.URI_NOT_ABSOLUTE)
                    .build();
        }
        else {
            return BusinessException.builder()
                    .errorCode(ErrorCode.UNKNOWN_EXCEPTION)
                    .errorMessage(errorMessage)
                    .build();
        }
    }
}