package com.itheima.test;

import com.itheima.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


public class InitMockOrSpyMethod2 {
    private UserService mockSerService;
    private UserService spyUserService;
    @BeforeEach
    public void init(){
        mockSerService = Mockito.mock(UserService.class);
        spyUserService = Mockito.spy(UserService.class);
    }

    @Test
    public void testMock() {
        //判断某对象是否Mock对象
        System.out.println(Mockito.mockingDetails(mockSerService).isMock());
        System.out.println(Mockito.mockingDetails(mockSerService).isSpy());

        System.out.println(Mockito.mockingDetails(spyUserService).isMock());
        System.out.println(Mockito.mockingDetails(spyUserService).isSpy());
    }
}
