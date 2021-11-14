package com.it.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.it.dao.UserDao;
import com.it.dto.UserDto;
import com.it.entity.ApiResult;
import com.it.entity.SysUser;
import com.it.entity.SysUserDO;
import com.it.exception.BizException;
import com.it.service.UserService;
import com.it.utils.DozerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: HSL
 * @Date: 2021/11/13 16:31
 * @Desc: 用户业务
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private DozerUtil dozerUtil;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public SysUser saveUser(UserDto userDto) {
        return null;
    }

    @Override
    public SysUser updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public SysUser getUser(String username) {
        return null;
    }

    @Override
    public void changePassword(String userName, String oldPassword, String newPassword) {
        SysUserDO user = userDao.getUser(userName);
        if (ObjectUtil.isNull(user)) {
            throw new BizException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BizException("原密码错误");
        }
        userDao.changePassword(user.getId(), passwordEncoder.encode(newPassword));
        log.debug("修改{}的密码", userName);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Override
    public ApiResult<String> create(UserDto user) {
        SysUserDO userDO = dozerUtil.map(user, SysUserDO.class);
        user.setCreateTime(new Date());
        userDao.insert(userDO);
        return ApiResult.success("保存成功");
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public ApiResult<String> edit(UserDto user) {
        if (ObjectUtil.isNull(user.getId())) {
            return ApiResult.fail("id为空");
        }
        SysUserDO sysUser = userDao.selectById(user.getId());
        if (ObjectUtil.isNull(sysUser)) {
            return ApiResult.fail("用户不存在或已删除");
        }
        SysUserDO update = dozerUtil.map(user, SysUserDO.class);
        update.setId(user.getId());
        update.setUpdateTime(new Date());
        userDao.update(update);
        return ApiResult.success("修改成功");
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Override
    public ApiResult<SysUser> getUserById(Long id) {
        return ApiResult.success(dozerUtil.map(userDao.getById(id), SysUser.class));
    }
}
