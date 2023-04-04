package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Account;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.LinkRef;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Links;
import com.gdn.developer.productivity.pullrequestnotifier.services.BitbucketService;
import com.gdn.developer.productivity.pullrequestnotifier.services.TeamsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private BitbucketService bitbucketService;

    @Mock
    private TeamsService teamsService;

    private static final String project_name = "Trial";
    private static Repository repository, repository1, emptyRepository;
    private static PullRequest pullRequest, pullRequest1;
    private static Links links;
    private static LinkRef html;
    private static Account author;
    private static List<Repository> repositoryList, emptyRepositoryList, fullRepositoryList;
    private static List<PullRequest> pullRequestList, pullRequestList1, emptyPullRequestList;

    @BeforeAll
    public static void init(){

        html = LinkRef.builder()
                .href("html.com")
                .build();

        links = Links.builder()
                .html(html)
                .build();

        author = Account.builder()
                .display_name("user")
                .build();

        repository = Repository.builder()
                .slug("repo 1")
                .build();

        repository1 = Repository.builder()
                .slug("repo 2")
                .build();

        emptyRepository = Repository.builder()
                .slug("empty repo")
                .build();

        pullRequest = PullRequest.builder()
                .id(1L)
                .title("initial commit")
                .links(links)
                .created_on("2023-03-27T05:41:50.953768+00:00")
                .author(author)
                .build();

        pullRequest1 = PullRequest.builder()
                .id(2L)
                .title("modified data")
                .links(links)
                .created_on("2023-03-27T05:41:50.953768+00:00")
                .author(author)
                .build();

        fullRepositoryList = new ArrayList<>(Arrays.asList(repository, repository1, emptyRepository));
        repositoryList = new ArrayList<>(Arrays.asList(repository));
        pullRequestList = new ArrayList<>(Arrays.asList(pullRequest));
        pullRequestList1 = Collections.nCopies(58, pullRequest1);
        emptyRepositoryList =  new ArrayList<>();
        emptyPullRequestList = new ArrayList<>();
    }

    @Test
    void sendPRNotificationsByProject_Success() throws BusinessException {

        when(bitbucketService.repositoryListByProjectName(project_name)).thenReturn(fullRepositoryList);
        when(bitbucketService.pullRequestListByRepoName("repo 1")).thenReturn(pullRequestList1);
        when(bitbucketService.pullRequestListByRepoName("repo 2")).thenReturn(pullRequestList);
        when(bitbucketService.pullRequestListByRepoName("empty repo")).thenReturn(emptyPullRequestList);
        when(teamsService.sendNotifications(Mockito.any(), Mockito.anyString()))
                .thenReturn(Boolean.TRUE);
        Boolean notificationsSent = notificationService.sendPRNotificationsByProject(project_name);
        verify(bitbucketService).repositoryListByProjectName(Mockito.anyString());
        verify(bitbucketService,times(3)).pullRequestListByRepoName(Mockito.anyString());
        verify(teamsService,atLeastOnce()).sendNotifications(Mockito.any(), Mockito.anyString());
        assertTrue(notificationsSent);
    }

    @Test
    void sendPRNotificationsByProject_InvalidProject() throws BusinessException{

        when(bitbucketService.repositoryListByProjectName(project_name)).thenReturn(emptyRepositoryList);
        assertThrows(BusinessException.class,()->{notificationService.sendPRNotificationsByProject(project_name);});
        verify(bitbucketService).repositoryListByProjectName(Mockito.anyString());
    }

    @Test
    void sendPRNotificationsByProject_RepositoryListError() throws BusinessException{

        when(bitbucketService.repositoryListByProjectName(project_name)).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class,()->{notificationService.sendPRNotificationsByProject(project_name);});
        verify(bitbucketService).repositoryListByProjectName(Mockito.anyString());
    }

    @Test
    void sendPRNotificationsByProject_PullRequestListError() throws BusinessException{

        when(bitbucketService.repositoryListByProjectName(project_name)).thenReturn(repositoryList);
        when(bitbucketService.pullRequestListByRepoName("repo 1")).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class,()->{notificationService.sendPRNotificationsByProject(project_name);});
        verify(bitbucketService).repositoryListByProjectName(Mockito.anyString());
        verify(bitbucketService).pullRequestListByRepoName(Mockito.anyString());
    }

    @Test
    void sendPRNotificationsByProject_SendNotificationError() throws BusinessException {

        when(bitbucketService.repositoryListByProjectName(project_name)).thenReturn(repositoryList);
        when(bitbucketService.pullRequestListByRepoName("repo 1")).thenReturn(pullRequestList);
        when(teamsService.sendNotifications(Mockito.any(), Mockito.anyString()))
                .thenThrow(BusinessException.class);
        assertThrows(BusinessException.class,()-> notificationService.sendPRNotificationsByProject(project_name));
        verify(bitbucketService).repositoryListByProjectName(Mockito.anyString());
        verify(bitbucketService).pullRequestListByRepoName(Mockito.anyString());
        verify(teamsService).sendNotifications(Mockito.any(), Mockito.anyString());
    }

}