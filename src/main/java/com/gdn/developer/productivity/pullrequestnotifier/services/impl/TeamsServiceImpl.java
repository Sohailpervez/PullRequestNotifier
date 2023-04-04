package com.gdn.developer.productivity.pullrequestnotifier.services.impl;
import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Fact;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Section;
import com.gdn.developer.productivity.pullrequestnotifier.services.TeamsService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ErrorMessage;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TeamsServiceImpl implements TeamsService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExceptionHelper exceptionHelper;

    @Value("#{${project.connector.map}}")
    private Map<String,String> projectConnectorMap;

    private Boolean sendTeamsNotification(MessageCard messageCard, String projectName) throws BusinessException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String connectorUrl = projectConnectorMap.get(projectName);
        if(connectorUrl == null){
            throw new BusinessException(ErrorCode.URI_NOT_PRESENT, ErrorMessage.URI_NOT_PRESENT);
        }
        try {
            HttpEntity<MessageCard> entity = new HttpEntity<>(messageCard,headers);
            restTemplate.exchange(connectorUrl, HttpMethod.POST, entity, String.class);
        }catch (Exception e){
            log.error("Error occured at sendTeamsNotification method in Project {} - error : {}", projectName,
                    e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean sendNotifications (Map<String, List<String>> prNotificationMap, String projectName)
            throws BusinessException{
        MessageCard messageCard=MessageCard.builder()
                .title(String.format("Project %s - Open Pull Request(s)", projectName))
                .build();
        if (prNotificationMap.isEmpty()){
            log.info("There are no open Pull Requests in the Project {}", projectName);
            messageCard.setText("**There are no Open Pull Requests in the Project**");
            sendTeamsNotification(messageCard, projectName);
            return Boolean.TRUE;
        }
        List<String> pullRequestList;
        List<Section> sections = new ArrayList<>();
        List<Fact> factList;
        Section section;
        int count = 0;
        Fact fact ;

        for(String repository: prNotificationMap.keySet()){
            pullRequestList = prNotificationMap.get(repository);
            factList = new ArrayList<>();
            section= Section.builder()
                    .activityTitle(String.format("Repository %s - Pull Request(s)", repository))
                    .build();
            count += 2;
            for(String prNotification :pullRequestList){
                fact = Fact.builder().value(prNotification).build();
                factList.add(fact);
                count += 1;
                if(count >= Constants.TEAMS_NOTIFICATION_LIMIT){
                    section.setFacts(factList);
                    sections.add(section);
                    messageCard.setSections(sections);
                    sendTeamsNotification(messageCard, projectName);
                    factList.clear();
                    count = 2;
                }
            }
            if(factList.isEmpty()){
                count = 0;
                continue;
            }
            section.setFacts(factList);
            sections.add(section);
        }
        if(count != 0) {
            messageCard.setSections(sections);
            sendTeamsNotification(messageCard, projectName);
        }
        return Boolean.TRUE;
    }
}
