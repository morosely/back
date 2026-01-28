package com.itheima.test;

import com.itheima.mapper.UserMapper;
import com.itheima.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InjectMocksTest {

    //@InjectMocks标注的属性必须是实现类，因为mockito不能创建接口的实现类，会创建对应的实例对象。默认创建的未经过mockito的实例对象，因此常常和@Spy注解使其变成默认调用真是对象的mock对象
    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void test1(){
        System.out.println("userService = " + userService);
        int number = userService.getNumber();
        Assertions.assertEquals(100, number);
    }
}
