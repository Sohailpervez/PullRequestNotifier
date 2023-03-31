package com.gdn.developer.productivity.pullrequestnotifier.utils;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class BitbucketHelperTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BitbucketHelper bitbucketHelper;

    private static final String token = "abcd";
    private static TokenResponse tokenResponse = TokenResponse.builder().access_token(token).build();
    private static ResponseEntity<TokenResponse> tokenResponseEntity =
            new ResponseEntity<>(tokenResponse, HttpStatus.OK);


    @Test
    void getToken_success() throws BusinessException {

        when(restTemplate.postForEntity(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(), (Class<TokenResponse>) ArgumentMatchers.any())).thenReturn(tokenResponseEntity);
        String actualToken = bitbucketHelper.getToken();
        assertEquals(token,actualToken);
    }
    @Test
    void getToken_throws_exception() {

        when(restTemplate.postForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any(),
                (Class<TokenResponse>) ArgumentMatchers.any())).thenThrow(new RuntimeException());
        assertThrows(Exception.class, ()-> bitbucketHelper.getToken());
    }
}