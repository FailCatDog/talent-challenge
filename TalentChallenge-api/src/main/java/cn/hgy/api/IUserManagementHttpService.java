package cn.hgy.api;

import cn.hgy.api.dto.AdminCreateRequestDTO;
import cn.hgy.api.dto.UserLoginRequestDTO;
import cn.hgy.api.dto.UserLoginResponseDTO;
import cn.hgy.api.dto.UserRegisterRequestDTO;
import cn.hgy.api.response.Response;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-27
 * @Description: 用户管理接口
 */
public interface IUserManagementHttpService {

    /**
     * 用户注册方法
     * @param dto 请求参数
     * @return 是否成功
     */
    Response<Boolean> userRegister(UserRegisterRequestDTO dto);

    /**
     * 用户登录
     *
     * @param dto 请求参数
     * @return 用户登录返回传输对象
     */
    Response<UserLoginResponseDTO> userLogin(UserLoginRequestDTO dto);

    /**
     * 管理员创建新管理员账号
     * @param dto 请求参数
     * @param request HTTP请求对象
     * @return 创建是否成功
     */
    Response<Boolean> createAdminAccount(AdminCreateRequestDTO dto, jakarta.servlet.http.HttpServletRequest request);
}
