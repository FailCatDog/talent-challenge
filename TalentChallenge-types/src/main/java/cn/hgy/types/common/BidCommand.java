package cn.hgy.types.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidCommand {

    private String bidId;
    private String projectId;
    private String teamId;
    private String content;
    private String attachmentUrl;
}

