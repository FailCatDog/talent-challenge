package cn.hgy.domain.user.service;

import cn.hgy.domain.user.model.valobj.UserStatisticsVO;
import cn.hgy.domain.user.repository.IUserManagementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户统计服务
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserStatisticsService {

    private final IUserManagementRepository userManagementRepository;

    /**
     * 获取用户统计数据
     */
    public UserStatisticsVO getUserStatistics() {
        return userManagementRepository.getUserStatistics();
    }
}
