package com.artist.sysadmin.dao;

import com.artist.sysadmin.domain.User;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

	List<User> findUserByRole(Long roleId);

	Long countByRoleId(Long roleId);

	List<String> receptByUsername(String username);

	int deleteUserRole(Long userId);

	List<User> findUserByMenu(Long menuId);

	int countByDepartmentId(Long deptId);

}