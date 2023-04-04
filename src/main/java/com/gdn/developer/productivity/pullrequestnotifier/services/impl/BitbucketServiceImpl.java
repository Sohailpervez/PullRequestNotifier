package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequestResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.ReposResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.services.BitbucketService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.BitbucketHelper;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Slf4j
@Service
public class BitbucketServiceImpl implements BitbucketService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BitbucketHelper bitbucketHelper;

    @Autowired
    private ExceptionHelper exceptionHelper;

    @Value("${repository.page.size:60}")
    private Integer repository_page_size;

    @Value("${pullrequest.page.size:30}")
    private Integer pullrequest_page_size;

    @Override
    public List<Repository> repositoryListByProjectName(String project_name) throws BusinessException {

        String repositoryListUrl = String.format("%srepositories/%s?q=project.name=\"%s\"&pagelen=%s",
                Constants.BITBUCKET_URL, Constants.WORKSPACE_SLUG, project_name, repository_page_size);

        HttpHeaders headers = getAuthorizedHeaders();
        try {
            HttpEntity<?> entity = new HttpEntity(headers);
            ResponseEntity<ReposResponse> response = restTemplate.exchange(repositoryListUrl, HttpMethod.GET, entity,
                    ReposResponse.class);
            ReposResponse reposResponse = response.getBody();
            List<Repository> repositoryList = reposResponse.getValues();
            while (reposResponse.getNext() != null) {
                response = restTemplate.exchange(reposResponse.getNext(), HttpMethod.GET, entity, ReposResponse.class);
                reposResponse = response.getBody();
                repositoryList.addAll(reposResponse.getValues());
            }
            return repositoryList;
        }
        catch (Exception e) {
            log.error("error occured at repositoryListByProjectName method in Project {} - error : {}", project_name,
                    e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
    }

    @Override
    public List<PullRequest> pullRequestListByRepoName(String repo_slug) throws BusinessException  {

        String pullRequestListUrl = String.format("%srepositories/%s/%s/pullrequests?state=OPEN&pagelen=%s",
                Constants.BITBUCKET_URL, Constants.WORKSPACE_SLUG, repo_slug, pullrequest_page_size);
        HttpHeaders headers = getAuthorizedHeaders();
        try {
            HttpEntity<?> entity = new HttpEntity(headers);
            ResponseEntity<PullRequestResponse> response = restTemplate.exchange(pullRequestListUrl, HttpMethod.GET, entity,
                    PullRequestResponse.class);
            PullRequestResponse pullRequestResponse = response.getBody();
            List<PullRequest> pullRequests = pullRequestResponse.getValues();
            while (pullRequestResponse.getNext() != null) {
                response = restTemplate.exchange(pullRequestResponse.getNext(), HttpMethod.GET, entity, PullRequestResponse.class);
                pullRequestResponse = response.getBody();
                pullRequests.addAll(pullRequestResponse.getValues());
            }
            return pullRequests;
        }
        catch (Exception e){
            log.error("error occured at pullRequestListByRepoName method - error : {}", e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
    }

    private HttpHeaders getAuthorizedHeaders() throws  BusinessException {

        HttpHeaders headers=new HttpHeaders();
        String token = bitbucketHelper.getToken();
        log.info("Acquired an Access Token");
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
