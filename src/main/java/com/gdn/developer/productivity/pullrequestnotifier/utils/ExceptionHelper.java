package com.gdn.developer.productivity.pullrequestnotifier.utils;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHelper {

    public BusinessException checkBusinessException(String errorMessage) {

        if(errorMessage.contains("Invalid OAuth client credentials")) {
            return new BusinessException(ErrorCode.INVALID_CREDENTIALS, ErrorMessage.INVALID_CREDENTIALS);
        }
        else if(errorMessage.contains("Token is invalid or not supported for this endpoint")) {
            return  new BusinessException(ErrorCode.UNAUTHORIZED, ErrorMessage.UNAUTHORIZED);
        }
        else if(errorMessage.contains("UnknownHostException")) {
            return new BusinessException(ErrorCode.UNKNOWN_HOST, ErrorMessage.UNKNOWN_HOST);
        }
        else if(errorMessage.contains("Network is unreachable")) {
            return new BusinessException(ErrorCode.NETWORK_UNREACHABLE, ErrorMessage.NETWORK_UNREACHABLE);
        }
        else if(errorMessage.contains("Invalid webhook URL")) {
            return new BusinessException(ErrorCode.INVALID_WEBHOOK_URL, ErrorMessage.INVALID_WEBHOOK_URL);
        }
        else {
            return new BusinessException(ErrorCode.UNKNOWN_EXCEPTION, errorMessage);
        }
    }
}