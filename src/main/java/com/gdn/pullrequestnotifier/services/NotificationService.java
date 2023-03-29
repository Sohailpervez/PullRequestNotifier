package com.gdn.pullrequestnotifier.services;

import org.springframework.web.client.RestClientException;

public interface NotificationService {

    Boolean sendPRNotificationsByProject(String project_name) throws RestClientException;
}
