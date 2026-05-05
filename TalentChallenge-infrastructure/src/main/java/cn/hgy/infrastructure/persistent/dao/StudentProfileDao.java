package cn.hgy.infrastructure.persistent.dao;

import cn.hgy.infrastructure.persistent.po.StudentProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学生资料Dao接口
 *
 * @author hgy
 */
@Mapper
public interface StudentProfileDao {

    /**
     * 根据用户ID查询学生资料
     */
    StudentProfile queryByUserId(@Param("userId") String userId);

    /**
     * 插入学生资料
     */
    int insert(StudentProfile studentProfile);

    /**
     * 根据用户ID更新学生资料
     */
    int updateByUserId(StudentProfile studentProfile);
}
