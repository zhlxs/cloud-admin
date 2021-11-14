package com.it.service;

import com.it.entity.Permission;

public interface PermissionService
{

	void save(Permission permission);

	void update(Permission permission);

	void delete(Long id);
}
