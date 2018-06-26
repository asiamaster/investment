package com.dili.uap.dao;

import com.dili.ss.base.MyMapper;
import com.dili.uap.domain.dto.UserDataDto;
import com.dili.uap.domain.dto.UserDto;
import com.dili.uap.sdk.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends MyMapper<User> {

    /**
     * 根据角色ID查询用户列表信息
     * @param roleId  角色ID
     * @return  用户列表
     */
    List<User> findUserByRole(Long roleId);

    /**
     * 根据条件联合查询数据信息
     * @param user
     * @return
     */
    List<UserDto> selectForPage(User user);

    /**
     * 根据用户ID查询用户所拥有的数据权限
     * @param params 查询参数(key:userId(用户ID)、firmCode(市场code))
     * @return
     */
    List<UserDataDto> selectUserDatas(Map<String,Object> params);
}