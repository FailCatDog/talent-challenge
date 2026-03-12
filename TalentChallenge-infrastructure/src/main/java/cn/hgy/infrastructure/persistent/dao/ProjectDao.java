package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目Mapper接口
 * 
 * @author hgy
 */
@Mapper
public interface ProjectDao {
    
    /**
     * 插入项目
     */
    int insert(Project project);
    
    /**
     * 根据项目ID更新项目
     */
    int updateByProjectId(Project project);
    
    /**
     * 根据项目ID查找项目
     */
    Project selectByProjectId(@Param("projectId") String projectId);

    /**
     * 根据项目ID查找项目（包含企业名称和团队名称）
     */
    Project selectByProjectIdWithNames(@Param("projectId") String projectId);
    
    /**
     * 根据流水号查找项目
     */
    Project selectBySerialNumber(@Param("serialNumber") String serialNumber);
    
    /**
     * 根据发布者ID查找项目
     */
    List<Project> selectByPublisherId(@Param("publisherId") String publisherId);
    
    /**
     * 根据状态查找项目
     */
    List<Project> selectByStatus(@Param("status") String status);
    
    /**
     * 根据分类ID查找项目
     */
    List<Project> selectByCategoryId(@Param("categoryId") String categoryId);
    
    /**
     * 查找已发布的项目
     */
    List<Project> selectPublishedProjects();
    
    /**
     * 分页查询项目
     */
    List<Project> selectByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 根据状态分页查询项目
     */
    List<Project> selectByStatusAndPage(@Param("status") String status,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);
    
    /**
     * 根据发布者ID分页查询项目
     */
    List<Project> selectByPublisherIdAndPage(@Param("publisherId") String publisherId,
                                             @Param("offset") int offset,
                                             @Param("limit") int limit);
    
    /**
     * 搜索项目（根据标题和描述）
     */
    List<Project> searchByKeyword(@Param("keyword") String keyword,
                                  @Param("offset") int offset,
                                  @Param("limit") int limit);
    
    /**
     * 根据标签搜索项目
     */
    List<Project> selectByTags(@Param("tags") List<String> tags,
                               @Param("offset") int offset,
                               @Param("limit") int limit);
    
    /**
     * 检查项目ID是否存在
     */
    int existsByProjectId(@Param("projectId") String projectId);
    
    /**
     * 检查流水号是否存在
     */
    int existsBySerialNumber(@Param("serialNumber") String serialNumber);
    
    /**
     * 根据项目ID删除项目
     */
    int deleteByProjectId(@Param("projectId") String projectId);
    
    /**
     * 统计项目总数
     */
    long count();
    
    /**
     * 根据状态统计项目数
     */
    long countByStatus(@Param("status") String status);
    
    /**
     * 根据发布者ID统计项目数
     */
    long countByPublisherId(@Param("publisherId") String publisherId);

    /**
     * 根据发布者ID和状态统计项目数
     */
    long countByPublisherIdAndStatus(@Param("publisherId") String publisherId,
                                     @Param("status") String status);

    /**
     * 根据发布者ID和状态分页查询项目
     */
    List<Project> selectByPublisherIdAndStatus(@Param("publisherId") String publisherId,
                                               @Param("status") String status,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    /**
     * 更新项目状态
     */
    int updateStatus(@Param("projectId") String projectId,
                    @Param("status") String status);
    
    /**
     * 统计热门项目（根据浏览量）
     */
    List<Project> selectPopularProjects(@Param("limit") int limit);
    
    /**
     * 统计最新项目
     */
    List<Project> selectLatestProjects(@Param("limit") int limit);
    
    /**
     * 增加浏览次数
     */
    int incrementViewCount(@Param("projectId") String projectId);
    
    /**
     * 增加收藏次数
     */
    int incrementFavoriteCount(@Param("projectId") String projectId);
    
    /**
     * 减少收藏次数
     */
    int decrementFavoriteCount(@Param("projectId") String projectId);
    
    /**
     * 增加申请次数
     */
    int incrementApplicationCount(@Param("projectId") String projectId);

    /**
     * 分页查询已发布项目
     */
    List<Project> selectPublishedByPage(@Param("offset") int offset,
                                        @Param("limit") int limit);

    /**
     * 根据分类ID分页查询已发布项目
     */
    List<Project> selectPublishedByCategoryId(@Param("categoryId") String categoryId,
                                              @Param("offset") int offset,
                                              @Param("limit") int limit);

    /**
     * 统计已发布项目数量
     */
    long countPublished();

    /**
     * 根据分类ID统计已发布项目数量
     */
    long countPublishedByCategoryId(@Param("categoryId") String categoryId);

    /**
     * 搜索项目
     */
    List<Project> searchProjects(@Param("keyword") String keyword,
                                 @Param("categoryId") String categoryId,
                                 @Param("skillLevel") String skillLevel,
                                 @Param("budgetMin") java.math.BigDecimal budgetMin,
                                 @Param("budgetMax") java.math.BigDecimal budgetMax,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    /**
     * 统计搜索结果数量
     */
    long countSearchResults(@Param("keyword") String keyword,
                           @Param("categoryId") String categoryId,
                           @Param("skillLevel") String skillLevel,
                           @Param("budgetMin") java.math.BigDecimal budgetMin,
                           @Param("budgetMax") java.math.BigDecimal budgetMax);

    /**
     * 更新项目状态和拒绝原因
     */
    int updateStatusAndReason(@Param("projectId") String projectId,
                              @Param("status") String status,
                              @Param("rejectReason") String rejectReason);

    /**
     * 更新项目中标团队ID
     */
    int updateAcceptedTeam(@Param("projectId") String projectId,
                          @Param("teamId") String teamId);

    /**
     * 根据承接团队ID分页查询项目
     */
    List<Project> selectByAcceptedTeamIdAndPage(@Param("teamId") String teamId,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);

    /**
     * 根据承接团队ID和状态分页查询项目
     */
    List<Project> selectByAcceptedTeamIdAndStatus(@Param("teamId") String teamId,
                                                  @Param("status") String status,
                                                  @Param("offset") int offset,
                                                  @Param("limit") int limit);

    /**
     * 根据承接团队ID统计项目数
     */
    long countByAcceptedTeamId(@Param("teamId") String teamId);

    /**
     * 根据承接团队ID和状态统计项目数
     */
    long countByAcceptedTeamIdAndStatus(@Param("teamId") String teamId,
                                       @Param("status") String status);

    /**
     * 按状态分组统计项目数量
     */
    List<Map<String, Object>> countGroupByStatus();

    /**
     * 统计指定时间之后创建的项目数量
     */
    long countByCreateTimeAfter(@Param("startTime") String startTime);

    /**
     * 按分类分组统计项目数量（关联project_category获取分类名称）
     */
    List<Map<String, Object>> countGroupByCategory();
}
