package cn.hgy.infrastructure.persistent.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Bid {

    private Long id;
    private String bidId;
    private String projectId;
    private String teamId;
    private String status;
    private String content;
    private String attachmentUrl;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long version;
}

