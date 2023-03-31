package com.gdn.developer.productivity.pullrequestnotifier.utils;

import java.util.HashMap;
import java.util.Map;

public interface Constants {

    String HYPHEN = "-";

    String CLIENT_ID = "E8NFVLvYBeDxNLKEcj";

    String SECRET_KEY = "2UTGhnQKAsWG8unGQxGgctfbjMTqzKs4";

    String BITBUCKET_URL = "https://api.bitbucket.org/2.0/";

    String AUTH_URL = "https://bitbucket.org/site/oauth2/access_token";

    String GRANT_TYPE_KEY = "grant_type";

    String GRANT_TYPE_VALUE = "client_credentials";

    Integer REPO_PAGE_SIZE = 60;

    Integer PULLREQUEST_PAGE_SIZE = 30;

    Integer TEAMS_NOTIFICATION_LIMIT = 7;

    String WORKSPACE_SLUG = "trial-test";

    String ACTIVITY_SUBTITLE = "Awaiting Code Review from Reviewers";

    String ACTIVITY_IMAGE = "https://cdn-images-1.medium.com/max/1200/1*FohIbRXGjqdSyanJglouVA.jpeg";

    String MESSAGECARD_TYPE = "MessageCard";

    String MESSAGECARD_CONTEXT = "http: //schema.org/extensions";

    String MESSAGECARD_SUMMARY = "Open Pull Request Details";

    String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";

    String ACCESS_TOKEN = "'accessToken'";

    Map<String, String> CONNECTOR_URLS = new HashMap<String, String>(){{
        put("trial","https://gdncomm.webhook.office.com/webhookb2/c56224fc-cf92-493b-8552-5c9443638e4c@f0d6e2fe-f005-4046-8dc1-16668be1de52/IncomingWebhook/2d155ed1163d4f0184feb575dce4999d/13a00c88-0350-4a31-92e7-267304d02b41");
    }};
}
