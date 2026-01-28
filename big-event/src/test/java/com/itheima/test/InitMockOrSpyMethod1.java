package com.itheima.test;

import com.itheima.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InitMockOrSpyMethod1 {
    @Mock
    private UserService mockSerService;
    @Spy
    private UserService spyUserService;

    @Test
    public void testMock() {
        //判断某对象是否Mock对象
        System.out.println(Mockito.mockingDetails(mockSerService).isMock());
        System.out.println(Mockito.mockingDetails(mockSerService).isSpy());

        System.out.println(Mockito.mockingDetails(spyUserService).isMock());
        System.out.println(Mockito.mockingDetails(spyUserService).isSpy());
    }
}
