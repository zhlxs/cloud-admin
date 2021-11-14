package com.it.config;

import com.it.entity.ApiResult;
import com.it.enumration.ExceptionCode;
import com.it.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ControllerAdvice(annotations = { RestController.class, Controller.class })
@ResponseBody
@Slf4j
public class ExceptionConfiguration
{
	public ExceptionConfiguration()
	{

	}

	@ExceptionHandler({ BizException.class })
	public ApiResult<String> bizException(BizException ex, HttpServletRequest request, HttpServletResponse response)
	{
		log.warn("bizException:", ex);
		response.setStatus(ExceptionCode.OPERATION_EX.getStatus());
		return ApiResult.result(ex.getCode(), "", ex.getMessage()).setPath(request.getRequestURI());
	}
}
