package com.gdn.developer.productivity.pullrequestnotifier.services;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;

import java.util.List;
import java.util.Map;

public interface TeamsService {
    Boolean sendNotifications(Map<String, List<String>> prNotificationMap, String projectName) throws BusinessException;
}
