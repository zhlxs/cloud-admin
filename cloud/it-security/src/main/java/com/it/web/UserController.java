package com.it.web;

import com.it.dto.UserDto;
import com.it.entity.ApiResult;
import com.it.entity.SysUser;
import com.it.exception.BizException;
import com.it.service.UserService;
import com.it.utils.SysUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务
 */
@Api(tags = "用户服务")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController
{
	@Autowired
	private UserService userService;

	@ApiOperation(value = "新增用户")
	@PostMapping("/create")
	public ApiResult<String> create(@RequestBody UserDto user)
	{
		ApiResult<String> result = userService.create(user);
		if (result.isSuccess())
		{
			return result;
		}
		throw BizException.wrap(result.getCode(), result.getMsg());
	}

	@ApiOperation(value = "修改用户")
	@PostMapping("/edit")
	public ApiResult<String> edit(@RequestBody UserDto user)
	{
		ApiResult<String> result = userService.edit(user);
		if (result.isSuccess())
		{
			return result;
		}
		throw BizException.wrap(result.getCode(), result.getMsg());
	}

	@ApiOperation(value = "修改头像")
	@PutMapping(value = "/editHeadImgUrl", params = "headImgUrl")
	public void updateHeadImgUrl(String headImgUrl)
	{
		SysUser user = SysUserUtil.getLoginUser();
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		dto.setHeadImgUrl(headImgUrl);
		userService.updateUser(dto);
		log.debug("{}修改了头像", user.getUsername());
	}

	@ApiOperation(value = "修改密码")
	@PutMapping("/changePwd/{userName}")
	public void changePwd(@PathVariable String userName, String oldPassword, String newPassword)
	{
		userService.changePassword(userName, oldPassword, newPassword);
	}

	@ApiOperation(value = "获取当前用户")
	@GetMapping("/getCurrentUser")
	public SysUser getCurrentUser()
	{
		return SysUserUtil.getLoginUser();
	}

	@ApiOperation(value = "根据id查询用户")
	@GetMapping("/{id}")
	public ApiResult<SysUser> getUserById(@PathVariable Long id)
	{
		return userService.getUserById(id);
	}
}
