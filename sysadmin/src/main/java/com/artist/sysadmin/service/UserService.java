package com.artist.sysadmin.service;

import com.artist.sysadmin.domain.Department;
import com.artist.sysadmin.domain.User;
import com.artist.sysadmin.domain.dto.*;
import com.artist.sysadmin.exception.UserException;
import com.dili.ss.base.BaseService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.artist.sysadmin.sdk.domain.UserTicket;

import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成 This file was generated on 2017-07-04 15:24:50.
 */
public interface UserService extends BaseService<User, Long> {

	UserLoginResultDto doLogin(UserLoginDto command) throws UserException;

	void disableUser(Long userId) throws UserException;

	void enableUser(Long userId) throws UserException;

	BaseOutput<Object> logicDelete(Long userId);

	BaseOutput<Object> add(AddUserDto dto);

	UserDepartmentDto update(UpdateUserDto dto) throws UserException;

	void logout(String sessionId);

	List<User> findUserByRole(Long roleId);

	BaseOutput<Object> updateUserPassword(UpdateUserPasswordDto dto);

	UserTicket fetchLoginUserInfo(Long userId);

	void refreshUserPermission(Long userId);

	List<Map> listOnlineUsers(User user) throws Exception;

	void kickUserOffline(Long userId);

	EasyuiPageOutput listPageUserDto(User user);

	/**
	 * 根据用户ID查询用户所属的部门信息
	 * @param userId 用户ID
	 * @return 用户所属部门信息
	 */
	List<Department> listUserDepartmentByUserId(Long userId);

	/**
	 * 调整余额(单位均为分)
	 * @param id	用户id
	 * @param amount	调整金额，正为加，负为减(单位分)
	 * @return	返回调整后余额
	 */
	BaseOutput<Long> adjustBalance(Long id, Long amount);

	/**
	 * 调整余额
	 * @param id	用户id
	 * @param amount	调整金额(前台传参，单位元，带小数)，正为加，负为减
	 * @param notes    备注
	 * @return	返回"调整余额成功"
	 */
	BaseOutput<String> adjustBalance(Long id, String amount, String notes);
}