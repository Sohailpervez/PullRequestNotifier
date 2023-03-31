package com.gdn.developer.productivity.pullrequestnotifier.services.impl;
import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.services.TeamsService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Override
    public Boolean sendNotification(MessageCard messageCard, String project_name) throws BusinessException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MessageCard> entity = new HttpEntity<>(messageCard,headers);
        try {
            restTemplate.exchange(projectConnectorMap.get(project_name), HttpMethod.POST, entity, String.class);
        }catch (Exception e){
            log.error("error occured at sendNotification method in Project {} - error : {}", project_name,
                    e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
        return Boolean.TRUE;
    }
}
