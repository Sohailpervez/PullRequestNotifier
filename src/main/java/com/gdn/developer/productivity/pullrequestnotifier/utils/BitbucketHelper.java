package com.gdn.developer.productivity.pullrequestnotifier.utils;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class BitbucketHelper {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExceptionHelper exceptionHelper;

    @Cacheable(value = "myCache", key = Constants.ACCESS_TOKEN)
    public String getToken() throws BusinessException {

        log.info("Getting access token from bitbucket");

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(Constants.CLIENT_ID, Constants.SECRET_KEY);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(Constants.GRANT_TYPE_KEY, Constants.GRANT_TYPE_VALUE);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        try{
        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(Constants.AUTH_URL,
                requestEntity, TokenResponse.class);

        TokenResponse responseBody = responseEntity.getBody();
        return responseBody.getAccess_token();

        }
        catch (Exception e){
            log.error("error occured at getToken method - error : {}", e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
    }

}
