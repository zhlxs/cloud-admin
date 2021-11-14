package com.it.service.impl;

import com.it.SecurityApplication;
import com.it.dao.UserDao;
import com.it.entity.SysUserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

//import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecurityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class UserServiceImplTest
{

	@Autowired
	private UserDao userDao;

	@Test
	public void saveUser()
	{
		SysUserDO user = new SysUserDO();
		user.setCreateTime(new Date());
		user.setBirthday(new Date());
		user.setEmail("1358311815@qq.com");
		user.setHeadImgUrl("/xx/xx");
		user.setIntro("虎山路");
		user.setNickname("山鸡");
		user.setPassword("123456");
		user.setPhone("18397860322");
		user.setSex(1);
		user.setStatus(1);
		user.setUsername("胡山林");
		user.setTelephone("07928888888");
		user.setId(1L);
		userDao.insert(user);
	}

	@Autowired
	private RestTemplate restTemplate;

	@Test
	public void test1(){
		String result = restTemplate.getForObject("http://192.168.1.102:8080/api/segment/get/leaf-segment-test",String.class);
		System.out.println(result);
	}
}