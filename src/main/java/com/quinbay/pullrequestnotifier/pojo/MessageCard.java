package com.quinbay.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageCard {
    private String type = "MessageCard";
    private String context = "http: //schema.org/extensions";
    private String summary = "Open Pull Request details";
    private List<Section> sections;
}
