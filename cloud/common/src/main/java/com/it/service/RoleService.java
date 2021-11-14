package com.it.service;

import com.it.dto.RoleDto;

public interface RoleService
{

	void saveRole(RoleDto roleDto);

	void deleteRole(Long id);
}
