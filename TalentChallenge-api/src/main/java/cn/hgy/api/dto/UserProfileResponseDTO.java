package cn.hgy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponseDTO {
    
    private String userId;
    
    private String nickname;
    
    private String avatarUrl;
    
    private String bio;
    
    private Integer gender;
    
    private LocalDate birthday;
    
    private String regionCode;
    
    private String industry;
    
    private String title;
    
    private LocalDateTime lastLoginTime;
}

