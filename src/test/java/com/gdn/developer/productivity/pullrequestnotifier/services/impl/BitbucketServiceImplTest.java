package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequestResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.ReposResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.utils.BitbucketHelper;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class BitbucketServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BitbucketHelper bitbucketHelper;

    @Mock
    private ExceptionHelper exceptionHelper;

    @InjectMocks
    private BitbucketServiceImpl bitbucketService;

    private static final String repo_slug = "main";
    private static final String PROJECT_NAME = "trial";
    public static final Repository repository1 = Repository.builder().build();

    private static String urlBuilder = new StringBuilder(Constants.BITBUCKET_URL).append("repositories/" +
            "%s?q=project.name=\"%s\"&pagelen=%s").toString();

    private static String prUrlBuilder = new StringBuilder(Constants.BITBUCKET_URL)
            .append("repositories/%s/%s/pullrequests?state=OPEN&pagelen=%s").toString();

    private static String repositoryListUrl = String.format(urlBuilder, Constants.WORKSPACE_SLUG, PROJECT_NAME,
            60);

    private static String repositoryListUrlWithNext = "abcd";
    private static String pullRequestListUrl = String.format(prUrlBuilder, Constants.WORKSPACE_SLUG, repo_slug,
            30);

    private static String pullRequestListUrlWithNext = "pullRequestListUrlWithNext";

    private static final String token = "token";
    public static final PullRequest pr1 = PullRequest.builder().id(1L).build();

    private static List<Repository> repositoryList = new ArrayList<>();
    private static List<PullRequest> pullRequestList = new ArrayList<>();

    private static PullRequestResponse prResponse = PullRequestResponse.builder().values(pullRequestList).build();
    private static PullRequestResponse prResponseWithNext = PullRequestResponse.builder().values(pullRequestList)
            .next(pullRequestListUrlWithNext).build();

    private static ReposResponse reposResponse = ReposResponse.builder().values(repositoryList).next(null).build();
    private static ReposResponse reposResponseWithNext = ReposResponse.builder().values(repositoryList)
            .next(repositoryListUrlWithNext).build();

    private static ResponseEntity<PullRequestResponse> prResponseEntity = new ResponseEntity<>(prResponse, HttpStatus.OK);
    private static ResponseEntity<PullRequestResponse> prResponseEntityWithNext = new ResponseEntity<>(prResponseWithNext,
            HttpStatus.OK);
    private static ResponseEntity<ReposResponse> reposResponseEntity =
            new ResponseEntity<>(reposResponse, HttpStatus.OK);
    private static ResponseEntity<ReposResponse> reposResponseEntityWithNext =
            new ResponseEntity<>(reposResponseWithNext, HttpStatus.OK);

    private static HttpHeaders headers = new HttpHeaders();
    private static HttpEntity entity;

    private static BusinessException businessException = new BusinessException();

    @BeforeAll
    public static void init() {
        headers.set("Authorization", "Bearer " + token);
        entity = new HttpEntity(headers);
        pullRequestList.add(pr1);
        repositoryList.add(repository1);
    }

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(bitbucketService,"repository_page_size",60);
        ReflectionTestUtils.setField(bitbucketService, "pullrequest_page_size", 30);
    }

    @Test
    void repositoryListByProjectName_throws_BusinessException_atGetToken() throws BusinessException {

        when(bitbucketHelper.getToken()).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class, ()-> bitbucketService
                .repositoryListByProjectName(PROJECT_NAME));
        verify(bitbucketHelper).getToken();
    }

    @Test
    void repositoryListByProjectName_throws_BusinessException_atGettingReposList() throws BusinessException {

        when(bitbucketHelper.getToken()).thenReturn(token);

        when(restTemplate.exchange(repositoryListUrl, HttpMethod.GET, entity,
                ReposResponse.class)).thenThrow(new RuntimeException("error occured"));

        when(exceptionHelper.checkBusinessException(Mockito.anyString())).thenReturn(businessException);

        assertThrows(BusinessException.class, ()-> bitbucketService.repositoryListByProjectName(PROJECT_NAME));

        verify(bitbucketHelper).getToken();

        verify(restTemplate).exchange(repositoryListUrl, HttpMethod.GET, entity, ReposResponse.class);
    }

    @Test
    void repositoryListByProjectName_success() throws BusinessException {

        when(bitbucketHelper.getToken()).thenReturn(token);

        when(restTemplate.exchange(repositoryListUrl, HttpMethod.GET, entity,
                ReposResponse.class)).thenReturn(reposResponseEntityWithNext);

        when(restTemplate.exchange(repositoryListUrlWithNext, HttpMethod.GET, entity,
                ReposResponse.class)).thenReturn(reposResponseEntity);

        List<Repository> repos = bitbucketService.repositoryListByProjectName(PROJECT_NAME);

        verify(bitbucketHelper).getToken();

        verify(restTemplate).exchange(repositoryListUrl, HttpMethod.GET, entity, ReposResponse.class);

        verify(restTemplate).exchange(repositoryListUrlWithNext, HttpMethod.GET, entity, ReposResponse.class);

        assertEquals(repositoryList, repos);

    }

    @Test
    void pullRequestListByRepoName_success() throws BusinessException {

        when(bitbucketHelper.getToken()).thenReturn(token);

        when(restTemplate.exchange(pullRequestListUrl , HttpMethod.GET, entity,
                PullRequestResponse.class)).thenReturn(prResponseEntityWithNext);

        when(restTemplate.exchange(pullRequestListUrlWithNext, HttpMethod.GET, entity,
                PullRequestResponse.class)).thenReturn(prResponseEntity);
        List<PullRequest> pullRequests = bitbucketService.pullRequestListByRepoName(repo_slug);

        verify(bitbucketHelper).getToken();

        verify(restTemplate, times(2)).exchange(ArgumentMatchers.anyString() ,
                ArgumentMatchers.any(), ArgumentMatchers.any(), (Class<PullRequestResponse>) ArgumentMatchers.any());

        verify(restTemplate).exchange(pullRequestListUrlWithNext, HttpMethod.GET, entity, PullRequestResponse.class);
        assertEquals(pullRequestList, pullRequests);

    }

    @Test
    void pullRequestListByRepoName_throws_BusinessException_atGetToken() throws BusinessException {

        when(bitbucketHelper.getToken()).thenThrow(BusinessException.class);
        assertThrows(BusinessException.class, ()-> bitbucketService.pullRequestListByRepoName(repo_slug));
        verify(bitbucketHelper).getToken();
    }

    @Test
    void pullRequestListByRepoName_throws_BusinessException_atGettingReposList() throws BusinessException {

        when(bitbucketHelper.getToken()).thenReturn(token);

        when(restTemplate.exchange(repositoryListUrl, HttpMethod.GET, entity,
                ReposResponse.class)).thenThrow(new RuntimeException("error occured"));

        when(exceptionHelper.checkBusinessException(Mockito.anyString())).thenReturn(businessException);

        assertThrows(BusinessException.class, ()-> bitbucketService.pullRequestListByRepoName(repo_slug));

        verify(bitbucketHelper).getToken();

        verify(exceptionHelper).checkBusinessException(Mockito.anyString());
    }
}