package com.gdn.pullrequestnotifier.services;

import com.gdn.pullrequestnotifier.pojo.PullRequest;
import com.gdn.pullrequestnotifier.pojo.Repository;
import org.springframework.web.client.RestClientException;

import java.util.List;

public interface BitbucketService {

    List<Repository> repositoryListByProjectName(String project_name, String token) throws RestClientException;

    List<PullRequest> pullRequestListByRepoName(String repo_slug, String token) throws RestClientException;

    String getToken() throws RestClientException;
}
