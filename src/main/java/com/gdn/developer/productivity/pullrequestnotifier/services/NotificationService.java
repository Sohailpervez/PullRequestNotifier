package com.gdn.developer.productivity.pullrequestnotifier.services;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;

public interface NotificationService {

    Boolean sendPRNotificationsByProject(String project_name) throws BusinessException;
}
