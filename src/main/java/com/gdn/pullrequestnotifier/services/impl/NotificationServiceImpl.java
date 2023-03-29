package com.gdn.pullrequestnotifier.services.impl;

import com.gdn.pullrequestnotifier.pojo.MessageCard;
import com.gdn.pullrequestnotifier.pojo.PullRequest;
import com.gdn.pullrequestnotifier.pojo.Repository;
import com.gdn.pullrequestnotifier.pojo.Section;
import com.gdn.pullrequestnotifier.pojo.Fact;
import com.gdn.pullrequestnotifier.services.BitbucketService;
import com.gdn.pullrequestnotifier.services.NotificationService;
import com.gdn.pullrequestnotifier.services.TeamsService;
import com.gdn.pullrequestnotifier.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

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

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);

    @Override
    public Boolean sendPRNotificationsByProject(String project_name) throws RestClientException {

        String access_token = bitbucketService.getToken();

        log.info("The access token is acquired");

        List<Repository> repositoryList = bitbucketService.repositoryListByProjectName(project_name, access_token);
        log.info(String.format("The number of repositories in project %s is ", project_name) + repositoryList.size());
        if(repositoryList.size() == 0){
            return Boolean.FALSE;
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
            pullRequestList = bitbucketService.pullRequestListByRepoName(repository.getSlug(), access_token);
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
//                    teamsService.sendNotification(messageCard);
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
//            teamsService.sendNotification(messageCard);
        }
        log.info(String.format("The notifications regarding Open Pull Requests of %s Project are sent", project_name));
        return Boolean.TRUE;
    }
}
