package com.it.config;

import com.it.utils.DozerUtil;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HSL
 * @date 2021-11-20 10:36
 * @desc
 */
@Configuration
public class DozerAutoConfiguration {

    @Bean
    public Mapper getMapper() {
        return new DozerBeanMapper();
    }

    @Bean
    public DozerUtil getDozerUtil() {
        return new DozerUtil(getMapper());
    }
}
