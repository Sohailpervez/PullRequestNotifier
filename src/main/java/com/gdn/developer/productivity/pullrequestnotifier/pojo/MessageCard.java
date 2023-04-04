package com.gdn.developer.productivity.pullrequestnotifier.pojo;

import com.gdn.developer.productivity.pullrequestnotifier.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCard {

    @Builder.Default
    private String type = Constants.MESSAGECARD_TYPE;
    @Builder.Default
    private String context = Constants.MESSAGECARD_CONTEXT;
    @Builder.Default
    private String summary = Constants.MESSAGECARD_SUMMARY;
    private String title;
    private String text;
    private List<Section> sections;
}
