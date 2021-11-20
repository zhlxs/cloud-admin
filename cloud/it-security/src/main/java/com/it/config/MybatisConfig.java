package com.it.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.it.interceptor.MybatisInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig
{
	@Bean
	ConfigurationCustomizer mybatisConfigurationCustomizer()
	{
		return configuration -> configuration.addInterceptor(new MybatisInterceptor());
	}
}
