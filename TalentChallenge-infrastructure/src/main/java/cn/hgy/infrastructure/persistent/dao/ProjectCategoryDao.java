package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.ProjectCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目分类Mapper接口
 * 
 * @author hgy
 */
@Mapper
public interface ProjectCategoryDao {
    
    /**
     * 插入项目分类
     */
    int insert(ProjectCategory category);
    
    /**
     * 根据分类ID更新分类
     */
    int updateByCategoryId(ProjectCategory category);
    
    /**
     * 根据分类ID查找分类
     */
    ProjectCategory selectByCategoryId(@Param("categoryId") String categoryId);
    
    /**
     * 根据名称查找分类
     */
    ProjectCategory selectByName(@Param("name") String name);
    
    /**
     * 查找所有分类
     */
    List<ProjectCategory> selectAll();
    
    /**
     * 查找所有可见分类
     */
    List<ProjectCategory> selectAllVisible();
    
    /**
     * 查找所有启用分类
     */
    List<ProjectCategory> selectAllActive();
    
    /**
     * 根据父分类ID查找子分类
     */
    List<ProjectCategory> selectByParentId(@Param("parentId") String parentId);
    
    /**
     * 查找所有一级分类
     */
    List<ProjectCategory> selectRootCategories();
    
    /**
     * 检查分类ID是否存在
     */
    int existsByCategoryId(@Param("categoryId") String categoryId);
    
    /**
     * 检查分类名称是否存在
     */
    int existsByName(@Param("name") String name);
    
    /**
     * 根据分类ID删除分类
     */
    int deleteByCategoryId(@Param("categoryId") String categoryId);
    
    /**
     * 统计分类总数
     */
    long count();
    
    /**
     * 统计启用分类数
     */
    long countActive();
}
