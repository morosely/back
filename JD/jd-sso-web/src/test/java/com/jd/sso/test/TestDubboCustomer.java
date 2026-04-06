package com.jd.sso.test;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jd.common.pojo.JDReturnResult;
import com.jd.sso.service.UserService;

public class TestDubboCustomer {

	@Test
	public void startCustomer() throws IOException{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[] { "classpath:spring/spring*.xml" });  
        context.start();  
  
        UserService service = (UserService) context.getBean("userService");  
        JDReturnResult result = service.checkData("zhangshan", 1);
        System.out.println(result.getData());  
        System.in.read();  
	}
}
