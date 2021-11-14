package com.it.dao;

import com.it.entity.PermissionDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Set;

@Mapper
public interface PermissionDao
{

	@Select("select * from sys_permission t order by t.sort")
	List<PermissionDO> listAll();

	@Select("select * from sys_permission t where t.type = 1 order by t.sort")
	List<PermissionDO> listParents();

	@Select("select distinct p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId inner join sys_role_user ru on ru.roleId = rp.roleId where ru.userId = #{userId} order by p.sort")
	List<PermissionDO> listByUserId(Long userId);

	@Select("select p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId where rp.roleId = #{roleId} order by p.sort")
	List<PermissionDO> listByRoleId(Long roleId);

	@Select("select * from sys_permission t where t.id = #{id}")
	PermissionDO getById(Long id);

	@Insert("insert into sys_permission(parentId, name, css, href, type, permission, sort) values(#{parentId}, #{name}, #{css}, #{href}, #{type}, #{permission}, #{sort})")
	int save(PermissionDO permission);

	@Update("update sys_permission t set parentId = #{parentId}, name = #{name}, css = #{css}, href = #{href}, type = #{type}, permission = #{permission}, sort = #{sort} where t.id = #{id}")
	int update(PermissionDO permission);

	@Delete("delete from sys_permission where id = #{id}")
	int delete(Long id);

	@Delete("delete from sys_permission where parentId = #{id}")
	int deleteByParentId(Long id);

	@Delete("delete from sys_role_permission where permissionId = #{permissionId}")
	int deleteRolePermission(Long permissionId);

	@Select("select ru.userId from sys_role_permission rp inner join sys_role_user ru on ru.roleId = rp.roleId where rp.permissionId = #{permissionId}")
	Set<Long> listUserIds(Long permissionId);
}
