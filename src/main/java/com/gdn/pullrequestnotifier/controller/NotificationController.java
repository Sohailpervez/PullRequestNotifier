package com.gdn.pullrequestnotifier.controller;

import com.gdn.pullrequestnotifier.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(value = "/sendNotificationsByProjectName/{project_name}")
    public ResponseEntity<String> sendNotificationsByProjectName(@PathVariable("project_name") String project_name){
        Boolean notification = Boolean.FALSE;
        try {
            notification = notificationService.sendPRNotificationsByProject(project_name);
        }
        catch (RestClientException e){
            log.error("Error occurred in sendNotificationsByProjectName, projectName: {}, errMsg: {}", project_name, e.getMessage(), e);
            return new ResponseEntity<>("Error.. Check log", HttpStatus.BAD_REQUEST);
        }

        if(notification.equals(Boolean.FALSE)){
            return new ResponseEntity<>("The Project is not valid or doesnt contain repositories", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Notifications are sent", HttpStatus.OK);
    }
}
