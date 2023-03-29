package com.gdn.pullrequestnotifier.services;

import com.gdn.pullrequestnotifier.pojo.MessageCard;
import org.springframework.web.client.RestClientException;

public interface TeamsService {
    Boolean sendNotification(MessageCard messageCard) throws RestClientException;
}
