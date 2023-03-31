package com.gdn.developer.productivity.pullrequestnotifier.services;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;

public interface TeamsService {
    Boolean sendNotification(MessageCard messageCard, String project_name) throws BusinessException;
}
