package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.UserAuthCredential;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户认证凭证Dao接口
 * 
 * @author hgy
 */
@Mapper
public interface UserAuthCredentialDao {
    
    /**
     * 插入认证凭证
     */
    int insert(UserAuthCredential credential);
    
    /**
     * 根据认证凭证ID更新
     */
    int updateByCredentialId(UserAuthCredential credential);
    
    /**
     * 根据认证凭证ID查找
     */
    UserAuthCredential selectByCredentialId(@Param("credentialId") String credentialId);
    
    /**
     * 根据认证类型和标识符查找
     */
    UserAuthCredential selectByIdentityTypeAndIdentifier(@Param("identityType") String identityType,
                                                         @Param("identifier") String identifier);
    
    /**
     * 根据用户ID查找所有认证凭证
     */
    List<UserAuthCredential> selectByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID和认证类型查找用户是否存在
     */
    UserAuthCredential selectByUserIdAndIdentityType(@Param("userId") String userId,
                                                     @Param("identityType") String identityType);

    /**
     * 通过认证类型和认证唯一标识符查询用户
     * @param identityType 认证类型
     * @param identifier 唯一标识符
     * @return 用户认证po对象
     */
    UserAuthCredential queryUserByIdentityTypeAndIdentifier(@Param("identityType") String identityType,
                                                            @Param("identifier") String identifier);

    int existsByIdentityTypeAndIdentifier(@Param("identityType") String identityType,
                                          @Param("identifier") String identifier);
    
    /**
     * 根据认证凭证ID删除
     */
    int deleteByCredentialId(@Param("credentialId") String credentialId);
    
    /**
     * 根据用户ID删除所有认证凭证
     */
    int deleteByUserId(@Param("userId") String userId);
    
    /**
     * 查找已验证的认证凭证
     */
    List<UserAuthCredential> selectVerifiedByUserId(@Param("userId") String userId);

}
