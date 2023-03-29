package com.gdn.pullrequestnotifier.utils;

public class Constants {

    public static final String CLIENT_ID = "E8NFVLvYBeDxNLKEcj";

    public static final String SECRET_KEY = "2UTGhnQKAsWG8unGQxGgctfbjMTqzKs4";

    public static final String CONNECTOR_URL = "https://gdncomm.webhook.office.com/webhookb2/" +
            "c56224fc-cf92-493b-8552-5c9443638e4c@f0d6e2fe-f005-4046-8dc1-16668be1de52/" +
            "IncomingWebhook/2d155ed1163d4f0184feb575dce4999d/13a00c88-0350-4a31-92e7-267304d02b41";

    public static final String BITBUCKET_URL = "https://api.bitbucket.org/2.0/";

    public static final String AUTH_URL = "https://bitbucket.org/site/oauth2/access_token";

    public static final String GRANT_TYPE_KEY = "grant_type";

    public static final String GRANT_TYPE_VALUE = "client_credentials";

    public static final Integer REPO_PAGE_SIZE = 60;

    public static final Integer PULLREQUEST_PAGE_SIZE = 30;

    public static final Integer TEAMS_NOTIFICATION_LIMIT = 7;

    public static final String WORKSPACE_SLUG = "trial-test";

    public static final String ACTIVITY_SUBTITLE = "Awaiting Code Review from Reviewers";

    public static final String ACTIVITY_IMAGE = "https://scontent.fblr25-1.fna.fbcdn.net/v/t39.30808-6/285175162_" +
            "477689484111656_5656989788934331208_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=twnt-Buh1WMAX929" +
            "Gjg&_nc_ht=scontent.fblr25-1.fna&oh=00_AfDY7MZLpObwCa5acWfVmRAQsnBoMlmE3MNO8hE6B3Xc2w&oe=6422AB31";

    public static final String MESSAGECARD_TYPE = "MessageCard";

    public static final String MESSAGECARD_CONTEXT = "http: //schema.org/extensions";

    public static final String MESSAGECARD_SUMMARY = "Open Pull Request Details";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
}
