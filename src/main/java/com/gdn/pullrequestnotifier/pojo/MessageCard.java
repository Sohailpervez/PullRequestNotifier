package com.gdn.pullrequestnotifier.pojo;

import com.gdn.pullrequestnotifier.utils.Constants;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MessageCard {

    @Builder.Default
    private String type = Constants.MESSAGECARD_TYPE;
    @Builder.Default
    private String context = Constants.MESSAGECARD_CONTEXT;
    @Builder.Default
    private String summary = Constants.MESSAGECARD_SUMMARY;
    private String title;
    private List<Section> sections;
}
