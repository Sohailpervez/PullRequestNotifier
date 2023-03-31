package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.pojo.Fact;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Section;
import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TeamsServiceImplTest {

    @InjectMocks
    TeamsServiceImpl teamsService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    ExceptionHelper exceptionHelper;

    private final static String project_name = "trial";
    private static MessageCard messageCard;
    private static Section section;
    private static Fact fact;
    private static List<Section> sectionList;
    private static List<Fact> factList;
    private static HttpHeaders headers;
    private static HttpEntity<MessageCard> entity;
    private static BusinessException businessException;

    @BeforeAll
    public static void init(){

        fact = Fact.builder()
                .value("Pull Request Details")
                .build();

        section = Section.builder()
                .activityTitle("Repository repo 1 - Pull Request(s)")
                .facts(factList)
                .build();

        messageCard = MessageCard.builder()
                .title("Project trial - Open Pull Request(s)")
                .sections(sectionList)
                .build();

        factList = new ArrayList<>(Arrays.asList(fact));
        sectionList = new ArrayList<>(Arrays.asList(section));

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(messageCard,headers);

        businessException = new BusinessException();
    }

    @Test
    void sendNotification_Success() throws BusinessException {
        when(restTemplate.exchange(Constants.CONNECTOR_URLS.get(project_name), HttpMethod.POST , entity, String.class))
                .thenReturn(null);
        Boolean notificationSent = teamsService.sendNotification(messageCard, project_name);
        verify(restTemplate).exchange(Constants.CONNECTOR_URLS.get(project_name), HttpMethod.POST , entity, String.class);
        assertTrue(notificationSent);
    }

    @Test
    void sendNotification_Error() {
        when(restTemplate.exchange(Constants.CONNECTOR_URLS.get(project_name), HttpMethod.POST , entity, String.class))
                .thenThrow(new RuntimeException("error"));
        when(exceptionHelper.checkBusinessException(Mockito.any())).thenReturn(businessException);
        assertThrows(BusinessException.class, ()->teamsService.sendNotification(messageCard,project_name));
        verify(restTemplate).exchange(Constants.CONNECTOR_URLS.get(project_name), HttpMethod.POST , entity, String.class);
        verify(exceptionHelper).checkBusinessException(Mockito.anyString());
    }
}