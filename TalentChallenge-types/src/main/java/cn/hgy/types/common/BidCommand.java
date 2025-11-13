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
    private String studentId;
    private String content;
    private String attachmentUrl;
}

