package com.gdn.developer.productivity.pullrequestnotifier.services.impl;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PRsResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.ReposResponse;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;
import com.gdn.developer.productivity.pullrequestnotifier.services.BitbucketService;
import com.gdn.developer.productivity.pullrequestnotifier.utils.BitbucketHelper;
import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import com.gdn.developer.productivity.pullrequestnotifier.utils.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<Repository> repositoryListByProjectName(String project_name) throws BusinessException {

        String urlBuilder = new StringBuilder(Constants.BITBUCKET_URL)
                .append("repositories/%s?q=project.name=\"%s\"&pagelen=%s").toString();
        String repositoryListUrl = String.format(urlBuilder, Constants.WORKSPACE_SLUG, project_name,
                Constants.REPO_PAGE_SIZE);

            HttpEntity entity = new HttpEntity(getHeadersWithToken());
        try {
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
            log.error("error occured at getToken method in Project {} - error : {}", project_name, e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
    }

    @Override
    public List<PullRequest> pullRequestListByRepoName(String repo_slug) throws BusinessException  {

        String urlBuilder = new StringBuilder(Constants.BITBUCKET_URL)
                .append("repositories/%s/%s/pullrequests?state=OPEN&pagelen=%s").toString();

        String pullRequestListUrl = String.format(urlBuilder, Constants.WORKSPACE_SLUG, repo_slug,
                Constants.PULLREQUEST_PAGE_SIZE);
            HttpEntity entity = new HttpEntity(getHeadersWithToken());
        try {
            ResponseEntity<PRsResponse> response = restTemplate.exchange(pullRequestListUrl, HttpMethod.GET, entity,
                    PRsResponse.class);
            PRsResponse pRsResponse = response.getBody();
            List<PullRequest> pullRequests = pRsResponse.getValues();
            while (pRsResponse.getNext() != null) {
                response = restTemplate.exchange(pRsResponse.getNext(), HttpMethod.GET, entity, PRsResponse.class);
                pRsResponse = response.getBody();
                pullRequests.addAll(pRsResponse.getValues());
            }
            return pullRequests;
        }
        catch (Exception e){
            log.error("error occured at getToken method - error : {}", e.getMessage(), e);
            throw exceptionHelper.checkBusinessException(e.getMessage());
        }
    }

    private HttpHeaders getHeadersWithToken() throws  BusinessException {

        HttpHeaders headers=new HttpHeaders();
        String token = bitbucketHelper.getToken();
        log.info("Acquired an Access Token");
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
