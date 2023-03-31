package com.gdn.developer.productivity.pullrequestnotifier.services;

import com.gdn.developer.productivity.pullrequestnotifier.exceptions.BusinessException;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.PullRequest;
import com.gdn.developer.productivity.pullrequestnotifier.pojo.Repository;

import java.util.List;

public interface BitbucketService {

    List<Repository> repositoryListByProjectName(String project_name) throws BusinessException;

    List<PullRequest> pullRequestListByRepoName(String repo_slug) throws BusinessException;
}
