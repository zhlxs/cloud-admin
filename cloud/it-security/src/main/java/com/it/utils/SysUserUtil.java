package com.it.utils;

import com.it.dto.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SysUserUtil
{
	public static LoginUser getLoginUser()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null)
		{
			if (authentication instanceof AnonymousAuthenticationToken)
			{
				return null;
			}
			if (authentication instanceof UsernamePasswordAuthenticationToken)
			{
				return (LoginUser) authentication.getPrincipal();
			}
		}
		return null;
	}
}
