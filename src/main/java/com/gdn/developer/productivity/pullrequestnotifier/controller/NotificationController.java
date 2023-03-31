package com.gdn.developer.productivity.pullrequestnotifier.controller;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Error;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.ResponseBody;
import com.gdn.developer.productivity.pullrequestnotifier.services.NotificationService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/sendNotificationsByProjectName")
    public ResponseBody<String> sendNotificationsByProjectName(@RequestParam("project_name") String project_name){

        try {
            notificationService.sendPRNotificationsByProject(project_name);
        }
        catch (BusinessException be){
            return new ResponseBody<>(Boolean.FALSE, null ,new Error(be.getErrorCode(), be.getErrorMessage()));
        }
        catch (Exception e){
            return new ResponseBody<>(Boolean.FALSE, null, new Error(ErrorCode.UNKNOWN_EXCEPTION,e.getMessage()));
        }
        return new ResponseBody<>(Boolean.TRUE, "Notifications are sent", null) ;
    }
}
