package com.gdn.developer.productivity.pullrequestnotifier.controller;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Error;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.ResponseBody;
import com.gdn.developer.productivity.pullrequestnotifier.services.NotificationService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
            return ResponseBody.<String>builder()
                    .success(Boolean.FALSE)
                    .data(null)
                    .error(Error.builder().errorCode(be.getErrorCode()).errorMessage(be.getErrorMessage()).build())
                    .build();
        }
        catch (Exception e){
            return ResponseBody.<String>builder()
                    .success(Boolean.FALSE)
                    .data(null)
                    .error(Error.builder().errorCode(ErrorCode.UNKNOWN_EXCEPTION).errorMessage(e.getMessage()).build())
                    .build();
        }
        return ResponseBody.<String>builder()
                .success(Boolean.TRUE)
                .data("Notifications are sent")
                .error(null)
                .build();
    }
}
