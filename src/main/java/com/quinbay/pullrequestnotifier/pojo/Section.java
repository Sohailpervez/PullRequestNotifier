package com.quinbay.pullrequestnotifier.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class Section {

    private String activityTitle;
    private String activitySubtitle;
    private String activityImage;
    private String text;
    private List<Fact> facts;
    private boolean markdown;
}
