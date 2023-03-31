package com.gdn.developer.productivity.pullrequestnotifier.utils;

public interface ErrorMessage {

    String INVALID_CREDENTIALS = "Invalid client_id or secret_key";

    String UNAUTHORIZED = "Token is invalid or not supported for this endpoint.";

    String UNKNOWN_HOST = "UnknownHostException";

    String INVALID_PROJECT = "The Project is not valid or doesn't contain repositories";

    String NETWORK_UNREACHABLE = "Network is unreachable (connect failed)";

    String INVALID_WEBHOOK_URL = "Invalid teams webhook url";

    String URI_NOT_ABSOLUTE = "URI is not absolute";
}
