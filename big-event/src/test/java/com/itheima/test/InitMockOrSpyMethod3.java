package com.itheima.test;

import com.itheima.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;


public class InitMockOrSpyMethod3 {

    @Mock
    private UserService mockSerService;
    @Spy
    private UserService spyUserService;
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
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
