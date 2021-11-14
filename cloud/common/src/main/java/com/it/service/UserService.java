package com.it.service;

import com.it.dto.UserDto;
import com.it.entity.ApiResult;
import com.it.entity.SysUser;

public interface UserService
{

	SysUser saveUser(UserDto userDto);

	SysUser updateUser(UserDto userDto);

	SysUser getUser(String username);

	void changePassword(String username, String oldPassword, String newPassword);

	/**
	 * 新增用户
	 *
	 * @param user
	 * @return
	 */
	ApiResult<String> create(UserDto user);

	/**
	 * 修改用户
	 *
	 * @param user
	 * @return
	 */
	ApiResult<String> edit(UserDto user);

	/**
	 * 根据id查询用户
	 *
	 * @param id
	 * @return
	 */
	ApiResult<SysUser> getUserById(Long id);
}
