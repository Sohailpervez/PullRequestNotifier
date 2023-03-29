package com.gdn.pullrequestnotifier.services.impl;

import com.gdn.pullrequestnotifier.pojo.MessageCard;
import com.gdn.pullrequestnotifier.services.TeamsService;
import com.gdn.pullrequestnotifier.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class TeamsServiceImpl implements TeamsService {

    @Autowired
    private RestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @Override
    public Boolean sendNotification(MessageCard messageCard) throws RestClientException{

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MessageCard> entity = new HttpEntity<>(messageCard,headers);
        restTemplate.exchange(Constants.CONNECTOR_URL, HttpMethod.POST, entity, String.class);
        return Boolean.TRUE;
    }
}
