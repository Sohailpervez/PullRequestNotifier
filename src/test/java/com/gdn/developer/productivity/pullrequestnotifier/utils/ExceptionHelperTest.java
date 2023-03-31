package com.gdn.developer.productivity.pullrequestnotifier.utils;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ExceptionHelperTest {

    @InjectMocks
    ExceptionHelper exceptionHelper;

    private static final String INVALID_CREDENTIALS = "Invalid OAuth client credentials";

    private static final String UNAUTHORIZED = "Token is invalid or not supported for this endpoint.";

    private static final String UNKNOWN_HOST = "UnknownHostException";

    private static final String NETWORK_UNREACHABLE = "Network is unreachable";

    private static final String INVALID_WEBHOOK_URL = "Invalid webhook URL";

    private static final String UNKNOWN_ERROR = "unknown error";

    private static BusinessException businessException;

    @Test
    void checkBusinessException_INVALID_CREDENTIALS() {
        businessException = exceptionHelper.checkBusinessException(INVALID_CREDENTIALS);
        assertEquals(ErrorCode.INVALID_CREDENTIALS, businessException.getErrorCode());
        assertEquals(ErrorMessage.INVALID_CREDENTIALS, businessException.getErrorMessage());
    }

    @Test
    void checkBusinessException_UNAUTHORIZED() {
        businessException = exceptionHelper.checkBusinessException(UNAUTHORIZED);
        assertEquals(ErrorCode.UNAUTHORIZED, businessException.getErrorCode());
        assertEquals(ErrorMessage.UNAUTHORIZED, businessException.getErrorMessage());
    }

    @Test
    void checkBusinessException_UNKNOWN_HOST() {
        businessException = exceptionHelper.checkBusinessException(UNKNOWN_HOST);
        assertEquals(ErrorCode.UNKNOWN_HOST, businessException.getErrorCode());
        assertEquals(ErrorMessage.UNKNOWN_HOST, businessException.getErrorMessage());
    }

    @Test
    void checkBusinessException_NETWORK_UNREACHABLE() {
        businessException = exceptionHelper.checkBusinessException(NETWORK_UNREACHABLE);
        assertEquals(ErrorCode.NETWORK_UNREACHABLE, businessException.getErrorCode());
        assertEquals(ErrorMessage.NETWORK_UNREACHABLE, businessException.getErrorMessage());
    }

    @Test
    void checkBusinessException_INVALID_WEBHOOK_URL() {
        businessException = exceptionHelper.checkBusinessException(INVALID_WEBHOOK_URL);
        assertEquals(ErrorCode.INVALID_WEBHOOK_URL, businessException.getErrorCode());
        assertEquals(ErrorMessage.INVALID_WEBHOOK_URL, businessException.getErrorMessage());
    }

    @Test
    void checkBusinessException_UNKNOWN_EXCEPTION() {
        businessException = exceptionHelper.checkBusinessException(UNKNOWN_ERROR);
        assertEquals(ErrorCode.UNKNOWN_EXCEPTION, businessException.getErrorCode());
        assertEquals(UNKNOWN_ERROR, businessException.getErrorMessage());
    }
}