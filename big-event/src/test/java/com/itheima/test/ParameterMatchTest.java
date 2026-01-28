package com.itheima.test;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ParameterMatchTest {
    @Mock
    private UserService mockUserService;
    @Spy
    private UserService spyUserService;

    @Test
    public void testFindByUserNameVerify() {
        mockUserService.register("Tom", "123456");
        Mockito.verify(mockUserService, Mockito.times(1)).register("Tom", "123456");
//        mockSerService.register("Tom", null);
//        //ArgumentMatchers.anyString()拦截任意字符串对象,anyLong,anyXXX,都不包括null
//        Mockito.verify(mockSerService, Mockito.times(1)).register(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
    }

    @Test
    public void testFindByUserNameAnyString() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("Tom");
        //ArgumentMatchers.anyString()拦截任意字符串对象
        Mockito.doReturn(user1).when(mockUserService).findByUserName(ArgumentMatchers.anyString());

        User userResult1 = mockUserService.findByUserName("tom");
        System.out.println("user = " + userResult1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("Jack");
        User userResult2 = mockUserService.findByUserName("Jack");
        System.out.println("user = " + userResult2);
    }
    @Test
    public void testFindByUserName() {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("Tom");
        Mockito.doReturn(user1).when(mockUserService).findByUserName("tom");
        User userResult1 = mockUserService.findByUserName("tom");
        System.out.println("user = " + userResult1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("Jack");
        User userResult2 = mockUserService.findByUserName("Jack");
        System.out.println("user = " + userResult2);
    }

}

