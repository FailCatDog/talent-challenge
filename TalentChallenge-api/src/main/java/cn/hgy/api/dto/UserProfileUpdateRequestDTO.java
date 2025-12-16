package cn.hgy.api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileUpdateRequestDTO {
    
    private String nickname;
    
    private String avatarUrl;
    
    private String bio;
    
    private Integer gender;
    
    private LocalDate birthday;
    
    private String regionCode;
    
    private String industry;
    
    private String title;
}

