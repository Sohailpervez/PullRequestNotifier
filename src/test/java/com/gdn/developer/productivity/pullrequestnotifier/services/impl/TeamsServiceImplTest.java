package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.pojo.Fact;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Section;
import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TeamsServiceImplTest {

    @InjectMocks
    TeamsServiceImpl teamsService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ExceptionHelper exceptionHelper;

    @Mock
    private static Map<String,String> projectConnectorMap = new HashMap<>();

    private final static String project_name = "trial";
    private static MessageCard messageCard;
    private static HttpHeaders headers;
    private static HttpEntity<MessageCard> entity;
    private static BusinessException businessException;

    @BeforeAll
    public static void init(){

        messageCard = MessageCard.builder()
                .title("Project trial - Open Pull Request(s)")
                .build();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(messageCard,headers);

        businessException = new BusinessException();
    }

    @Test
    void sendNotification_Success() throws BusinessException {
        when(projectConnectorMap.get(Mockito.anyString())).thenReturn("connector url");
        when(restTemplate.exchange("connector url", HttpMethod.POST, entity, String.class))
                .thenReturn(null);
        Boolean notificationSent = teamsService.sendNotification(messageCard, project_name);
        verify(restTemplate).exchange(ArgumentMatchers.anyString(), ArgumentMatchers.any() , ArgumentMatchers.any(),
                (Class<Object>) ArgumentMatchers.any());
        assertTrue(notificationSent);
    }

    @Test
    void sendNotification_Error() {
        when(projectConnectorMap.get(Mockito.anyString())).thenReturn("connector url");
        when(restTemplate.exchange("connector url", HttpMethod.POST , entity, String.class))
                .thenThrow(new RuntimeException("error"));
        when(exceptionHelper.checkBusinessException(Mockito.any())).thenReturn(businessException);
        assertThrows(BusinessException.class, ()->teamsService.sendNotification(messageCard,project_name));
        verify(projectConnectorMap).get(Mockito.anyString());
        verify(restTemplate).exchange("connector url", HttpMethod.POST , entity, String.class);
        verify(exceptionHelper).checkBusinessException(Mockito.anyString());
    }

    @Test
    void sendNotification_TestError() {
        when(projectConnectorMap.get(Mockito.anyString())).thenReturn(null);
        when(restTemplate.exchange(null, HttpMethod.POST , entity, String.class))
                .thenThrow(new RuntimeException("error"));
        when(exceptionHelper.checkBusinessException(Mockito.any())).thenReturn(businessException);
        assertThrows(BusinessException.class, ()->teamsService.sendNotification(messageCard,project_name));
        verify(projectConnectorMap).get(Mockito.anyString());
        verify(exceptionHelper).checkBusinessException(Mockito.anyString());
    }
}