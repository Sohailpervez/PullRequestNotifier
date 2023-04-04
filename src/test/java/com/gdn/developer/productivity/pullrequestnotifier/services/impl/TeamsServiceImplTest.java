package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TeamsServiceImplTest {

    @InjectMocks
    private TeamsServiceImpl teamsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ExceptionHelper exceptionHelper;

    private final static String project_name = "trial";
    private static MessageCard messageCard;
    private static HttpHeaders headers;
    private static HttpEntity<MessageCard> entity;
    private static BusinessException businessException;
    private static Map<String,String> connectorMap = new HashMap<>();

    private static Map<String , List<String>> emptyPrNotificationMap = new HashMap<>();

    private static Map<String , List<String>> prNotificationMap = new HashMap<>();

    private static List<String> notificationList, notificationList2, notificationList3;

    @BeforeAll
    public static void init(){

        messageCard = MessageCard.builder()
                .title("Project trial - Open Pull Request(s)")
                .build();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(messageCard,headers);

        businessException = new BusinessException();

        connectorMap.put("trial", "connector url");
        notificationList = Collections.nCopies(58, "Open Pull Request of Repository 1");
        notificationList2 = Collections.nCopies(88, "Open Pull Request of Repository 2");
        notificationList3 = Collections.nCopies(25, "Open Pull Request of Repository 3");

        prNotificationMap = new HashMap<>();
        prNotificationMap.put("Repository 1", notificationList);
        prNotificationMap.put("Repository 2", notificationList2);
        prNotificationMap.put("Repository 3", notificationList3);
    }

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(teamsService, "projectConnectorMap", connectorMap);
    }
    @Test
    void sendNotification_Success() throws BusinessException {
        when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), (Class<Object>) Mockito.any()))
                .thenReturn(null);
        Boolean notificationSent = teamsService.sendNotifications(prNotificationMap, project_name);
        verify(restTemplate,Mockito.times(3)).exchange(ArgumentMatchers.anyString(),
                ArgumentMatchers.any() , ArgumentMatchers.any(),
                (Class<Object>) ArgumentMatchers.any());
        assertTrue(notificationSent);
    }
    @Test
    void sendNotification_emptyPrNotificationMap() throws BusinessException {
        when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), (Class<Object>) Mockito.any()))
                .thenReturn(null);
        Boolean notificationSent = teamsService.sendNotifications(emptyPrNotificationMap, project_name);
        verify(restTemplate).exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any() , ArgumentMatchers.any(),
                (Class<Object>) ArgumentMatchers.any());
        assertTrue(notificationSent);
    }

    @Test
    void sendTeamsNotification_ThrowsBusinessException() throws BusinessException {
        assertThrows(BusinessException.class, ()->teamsService.sendNotifications(prNotificationMap,"edit"));
    }
    @Test
    void sendTeamsNotification_ThrowsException() throws BusinessException {
        when(restTemplate.exchange(Mockito.anyString(), Mockito.any(), Mockito.any(), (Class<Object>) Mockito.any()))
                .thenThrow(new RuntimeException("error"));
        when(exceptionHelper.checkBusinessException(Mockito.anyString())).thenReturn(businessException);

        assertThrows(BusinessException.class, ()->teamsService.sendNotifications(prNotificationMap,project_name));
        verify(restTemplate).exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any() , ArgumentMatchers.any(),
                (Class<Object>) ArgumentMatchers.any());
        verify(exceptionHelper).checkBusinessException(Mockito.anyString());

    }

}