package com.gdn.pullrequestnotifier.services.impl;

import com.gdn.pullrequestnotifier.pojo.ReposResponse;
import com.gdn.pullrequestnotifier.pojo.Repository;
import com.gdn.pullrequestnotifier.pojo.PRsResponse;
import com.gdn.pullrequestnotifier.pojo.PullRequest;
import com.gdn.pullrequestnotifier.pojo.TokenResponse;
import com.gdn.pullrequestnotifier.services.BitbucketService;
import com.gdn.pullrequestnotifier.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class BitbucketServiceImpl implements BitbucketService {

    @Autowired
    private RestTemplate restTemplate;

    private final HttpHeaders headers = new HttpHeaders();

    @Override
    public List<Repository> repositoryListByProjectName(String project_name, String token) throws RestClientException{

//        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        String repositoryListUrl = String.format(Constants.BITBUCKET_URL+"repositories/%s?q=project.name=\"%s\"" +
                "&pagelen=%s", Constants.WORKSPACE_SLUG, project_name, Constants.REPO_PAGE_SIZE);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ReposResponse> response = restTemplate.exchange(repositoryListUrl, HttpMethod.GET, entity,
                ReposResponse.class);
        ReposResponse reposResponse = response.getBody();
        List<Repository> repositoryList = reposResponse.getValues();
        while (reposResponse.getNext() != null){
            response = restTemplate.exchange(reposResponse.getNext(), HttpMethod.GET, entity, ReposResponse.class);
            reposResponse = response.getBody();
            repositoryList.addAll(reposResponse.getValues());
        }
        return repositoryList;
    }

    @Override
    public List<PullRequest> pullRequestListByRepoName(String repo_slug, String token) throws RestClientException {

//        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        String stringBuilder = new StringBuilder(Constants.BITBUCKET_URL)
                .append("repositories/%s/%s/pullrequests?").append("state=OPEN&pagelen=%s").toString();

        String pullRequestListUrl = String.format(stringBuilder.toString(), Constants.WORKSPACE_SLUG, repo_slug, Constants.PULLREQUEST_PAGE_SIZE);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PRsResponse> response = restTemplate.exchange(pullRequestListUrl, HttpMethod.GET, entity,
                PRsResponse.class);
        PRsResponse pRsResponse = response.getBody();
        List<PullRequest> pullRequests = pRsResponse.getValues();
        while (pRsResponse.getNext() != null){
            response = restTemplate.exchange(pRsResponse.getNext(), HttpMethod.GET, entity, PRsResponse.class);
            pRsResponse = response.getBody();
            pullRequests.addAll(pRsResponse.getValues());
        }
        return pullRequests;
    }

    @Override
    @Cacheable(value = "myCache", key = "'accessToken'")
    public String getToken() throws RestClientException {

        log.info("Getting access token from bitbucket");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(Constants.CLIENT_ID, Constants.SECRET_KEY);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(Constants.GRANT_TYPE_KEY, Constants.GRANT_TYPE_VALUE);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TokenResponse> responseEntity = restTemplate.postForEntity(Constants.AUTH_URL,
                requestEntity, TokenResponse.class);

        TokenResponse responseBody = responseEntity.getBody();

        return responseBody.getAccess_token();
    }
}
