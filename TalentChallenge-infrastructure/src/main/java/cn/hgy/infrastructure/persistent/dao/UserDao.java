package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户Dao接口
 * 
 * @author hgy
 */
@Mapper
public interface UserDao {
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 根据用户ID更新用户
     */
    int updateByUserId(User user);

    /**
     * 根据用户ID查找用户
     * @param userId 逻辑id
     * @return 用户po对象
     */
    User queryByUserId(@Param("userId") String userId);
    
    /**
     * 根据昵称查找用户
     */
    List<User> selectByNickname(@Param("nickname") String nickname);
    
    /**
     * 检查用户ID是否存在
     */
    int existsByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID删除用户
     */
    int deleteByUserId(@Param("userId") String userId);
    
    /**
     * 查找所有激活的用户
     */
    List<User> selectAllActive();
    
    /**
     * 分页查询用户
     */
    List<User> selectByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计用户总数
     */
    long count();
    
    /**
     * 统计激活用户数
     */
    long countActive();

    /**
     * 更新用户最后登录时间
     * @param userId 用户id
     * @param lastLoginTime 最后登陆时间
     */
    void updateUserLastLoginTime(String userId, LocalDateTime lastLoginTime);

    /**
     * 更新用户资料信息（仅更新可编辑字段）
     * @param user 用户对象
     */
    int updateProfileByUserId(User user);
}
