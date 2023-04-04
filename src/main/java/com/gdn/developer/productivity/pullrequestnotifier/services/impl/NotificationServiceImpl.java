package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.services.BitbucketService;
import com.gdn.developer.productivity.pullrequestnotifier.services.NotificationService;
import com.gdn.developer.productivity.pullrequestnotifier.services.TeamsService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ErrorMessage;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import com.gdn.developer.productivity.pullrequestnotifier.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private BitbucketService bitbucketService;

    @Autowired
    private TeamsService teamsService;

    @Autowired
    private ExceptionHelper exceptionHelper;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);

    @Override
    public Boolean sendPRNotificationsByProject(String project_name) throws BusinessException {

        List<Repository> repositoryList = bitbucketService.repositoryListByProjectName(project_name);

        log.info("The number of repositories in project {} is {} ", project_name, repositoryList.size());
        if(repositoryList.isEmpty()){
            log.error("Error occured at sendPRNotificationsByProject method in Project {} - error : {}", project_name,
                    ErrorMessage.INVALID_PROJECT);
            throw new BusinessException(ErrorCode.INVALID_PROJECT, ErrorMessage.INVALID_PROJECT);
        }

        Map<String, List<String>> prNotificationMap = new HashMap<>();
        List<PullRequest> pullRequestList;
        String notification = new StringBuilder("The Pull Request [%s](%s) created by *%s*, is open from %s hours")
                .append(" and needs to be approved/merged").toString();

        LocalDateTime createdTime;
        long hours;

        for(Repository repository:repositoryList){
            List<String> notifications = new ArrayList<>();
            pullRequestList = bitbucketService.pullRequestListByRepoName(repository.getSlug());
            log.info(String.format("The number of open pull requests in %s repository is ", repository.getSlug()) +
                    pullRequestList.size());
            if(pullRequestList.isEmpty()){
                continue;
            }
            for (PullRequest pullRequest: pullRequestList){
                createdTime = LocalDateTime.parse(pullRequest.getCreated_on(), formatter);
                hours = Duration.between(createdTime, LocalDateTime.now(ZoneId.of(Constants.UTC))).toHours();
                notifications.add(String.format(notification, pullRequest.getTitle(),
                        pullRequest.getLinks().getHtml().getHref(), pullRequest.getAuthor().getDisplay_name(),
                        hours));
            }
            prNotificationMap.put(repository.getSlug(), notifications);
        }
        teamsService.sendNotifications(prNotificationMap, project_name);
        log.info(String.format("The notifications regarding Open Pull Requests of %s Project are sent", project_name));
        return Boolean.TRUE;
    }
}
