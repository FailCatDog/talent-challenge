package cn.hgy.types.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidConfirmedEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String bidId;
    
    private String projectId;
    
    private String teamId;
    
    private String operatorId;
    
    private LocalDateTime confirmedAt;
}

