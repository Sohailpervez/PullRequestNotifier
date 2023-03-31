package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.MessageCard;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Section;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Fact;
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
import java.util.List;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private BitbucketService bitbucketService;

    @Autowired
    private TeamsService teamsService;

    @Autowired
    private ExceptionHelper exceptionHelper;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);

    @Override
    public Boolean sendPRNotificationsByProject(String project_name) throws BusinessException {

        List<Repository> repositoryList = bitbucketService.repositoryListByProjectName(project_name);

        log.info("The number of repositories in project {} is {} ", project_name, repositoryList.size());
        if(repositoryList.size() == 0){
            log.error("error occured at sendPRNotificationsByProject method in Project {} - error : {}", project_name,
                    ErrorMessage.INVALID_PROJECT);
            throw new BusinessException(ErrorCode.INVALID_PROJECT, ErrorMessage.INVALID_PROJECT);
        }
        MessageCard messageCard=MessageCard.builder()
                .title(String.format("Project %s - Open Pull Request(s)", project_name))
                .build();

        Section section;
        Fact fact = Fact.builder().build();

        List<PullRequest> pullRequestList;
        List<Section> sections = new ArrayList<>();
        List<Fact> factList = new ArrayList<>();

        int count = 0;
        LocalDateTime createdTime;
        long hours;

        for(Repository repository:repositoryList){
            pullRequestList = bitbucketService.pullRequestListByRepoName(repository.getSlug());
            log.info(String.format("The number of open pull requests in %s repository is ", repository.getSlug()) +
                    pullRequestList.size());
            if(pullRequestList.size() == 0){
                continue;
            }
            section=Section.builder()
                    .activityTitle(String.format("Repository %s - Pull Request(s)", repository.getSlug()))
                    .build();
            count += 2;
            for(PullRequest pullRequest:pullRequestList){
                createdTime = LocalDateTime.parse(pullRequest.getCreated_on(), formatter);
                hours = Duration.between(createdTime, LocalDateTime.now(ZoneId.of("UTC"))).toHours();
                fact.setValue(String.format("The Pull Request [%s](%s) created by *%s*, is open from %s hours and " +
                        "needs to be approved/merged.", pullRequest.getTitle(),
                        pullRequest.getLinks().getHtml().getHref(), pullRequest.getAuthor().getDisplay_name(),
                        String.valueOf(hours)));
                factList.add(fact);
                count += 1;
                if(count >= Constants.TEAMS_NOTIFICATION_LIMIT){
                    section.setFacts(factList);
                    sections.add(section);
                    messageCard.setSections(sections);
                    teamsService.sendNotification(messageCard, project_name);
                    factList.clear();
                    sections.clear();
                    count = 2;
                }
                else {
                    fact = Fact.builder().build();
                }
            }
            if(factList.size() == 0){
                count = 0;
                continue;
            }
            section.setFacts(factList);
            sections.add(section);
            factList = new ArrayList<>();
        }
        if(count != 0) {
            messageCard.setSections(sections);
            teamsService.sendNotification(messageCard, project_name);
        }
        log.info(String.format("The notifications regarding Open Pull Requests of %s Project are sent", project_name));
        return Boolean.TRUE;
    }
}
