package com.gdn.developer.productivity.pullrequestnotifier.controller;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    private static final String project_name = "trial";

    @Test
    void sendNotificationsByProjectName_Success() throws Exception {

        when(notificationService.sendPRNotificationsByProject(project_name)).thenReturn(Boolean.TRUE);
        mockMvc.perform(post("/sendNotificationsByProjectName").param("project_name", project_name)
        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(jsonPath("$.data").value("Notifications are sent"))
                .andExpect(jsonPath("$.error").doesNotExist());
        verify(notificationService).sendPRNotificationsByProject(Mockito.anyString());
    }

    @Test
    void sendNotificationsByProjectName_ThrowsBusinessException() throws Exception{

        when(notificationService.sendPRNotificationsByProject(project_name)).thenThrow(BusinessException.class);
        mockMvc.perform(post("/sendNotificationsByProjectName").param("project_name", project_name)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
        verify(notificationService).sendPRNotificationsByProject(Mockito.anyString());
    }

    @Test
    void sendNotificationsByProjectName_ThrowsException() throws Exception{

        when(notificationService.sendPRNotificationsByProject(project_name))
                .thenThrow(new RuntimeException("unknown error"));
        mockMvc.perform(post("/sendNotificationsByProjectName").param("project_name", project_name)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
        verify(notificationService).sendPRNotificationsByProject(Mockito.anyString());
    }
}