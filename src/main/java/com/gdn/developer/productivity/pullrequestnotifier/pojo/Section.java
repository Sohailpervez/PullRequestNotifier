package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Section {

    private String activityTitle;
    @Builder.Default
    private String activitySubtitle = Constants.ACTIVITY_SUBTITLE;
    @Builder.Default
    private String activityImage = Constants.ACTIVITY_IMAGE;
    private List<Fact> facts;
    @Builder.Default
    private boolean markdown = Boolean.TRUE;
}
