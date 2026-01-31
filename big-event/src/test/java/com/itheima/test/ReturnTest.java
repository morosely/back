package com.itheima.test;

import com.itheima.pojo.User;
import com.itheima.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReturnTest {

    @Mock
    private List<String> mockList;
    @Mock
    private UserServiceImpl mockUserService;
    @Spy
    private UserServiceImpl spyUserService;

    //verify
    @Test
    public void test8(){
        mockList.add("one");
        //true;调用mock对象的写操作是没效果的
        Assertions.assertEquals(0, mockList.size());
        //验证调用过一次add方法，且参数是one
        verify(mockList).add("one");//指定调用的方法和参数，不会真实调用
        mockList.clear();
        verify(mockList, Mockito.times(1)).clear();

        //校验没有调用的两种方式
        verify(mockList, Mockito.never()).add("two");
        verify(mockList, Mockito.times(0)).add("two");

        //校验最少，最多
        verify(mockList, Mockito.atLeast(1)).add("one");
        verify(mockList, Mockito.atMost(1)).add("one");
    }

    //调用真实的方法
    @Test
    public void test7(){
        //mock不插桩，不会执行真实方法，返回默认值
        int number1 = mockUserService.getNumber();
        Assertions.assertEquals(0, number1);

        //对Mock进行插桩，让执行原来的真实方法
        when(mockUserService.getNumber()).thenCallRealMethod();
        int number2 = mockUserService.getNumber();
        Assertions.assertEquals(100, number2);

        //spy对象不插桩，会执行真实方法，返回真实方法返回值
        int number3 = spyUserService.getNumber();
        Assertions.assertEquals(100, number3);

        //对Spy对象进行插桩，when会执行原来的真实方法，返回mock的值
        when(spyUserService.getNumber()).thenReturn(200);
        int number4 = spyUserService.getNumber();
        Assertions.assertEquals(200, number4);

        //对Spy对象进行插桩，doXxx不会执行真实方法
        doReturn(300).when(spyUserService).getNumber();
        int number5 = spyUserService.getNumber();
        Assertions.assertEquals(300, number5);
    }

    //thenAnswer实现指定逻辑的插桩
    @Test
    public void test6(){
        when(mockList.get(anyInt())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                //argument表示插桩的方法(此处就是list.get())的第几个参数值
                Integer argument = invocationOnMock.getArgument(0, Integer.class);
                return String.valueOf(argument * 100);
            }
        });
        String result = mockList.get(1);
        Assertions.assertEquals("100", mockList.get(1));
    }

    //多次插桩
    @Test
    public void test5(){
        //第一次调用返回1，第二次返回2，第三次返回3，第3次以后都是返回3
        when(mockList.size()).thenReturn(1).thenReturn(2).thenReturn(3);
        Assertions.assertEquals(1, mockList.size());
        Assertions.assertEquals(2, mockList.size());
        Assertions.assertEquals(3, mockList.size());
        Assertions.assertEquals(3, mockList.size());
        //简化写法
        when(mockList.size()).thenReturn(1,2,3);
        Assertions.assertEquals(1, mockList.size());
        Assertions.assertEquals(2, mockList.size());
        Assertions.assertEquals(3, mockList.size());
        Assertions.assertEquals(3, mockList.size());
    }

    //异常
    @Test
    public void test4(){
        //方法1
        //doXXX无返回值
        doThrow(RuntimeException.class).when(mockList).clear();
        try {
            mockList.clear();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof RuntimeException);
        }
        //doXXX有返回值
//        doThrow(RuntimeException.class).when(mockList).get(anyInt());
//        try {
//            mockList.get(1);
//            Assertions.fail();
//        } catch (Exception e) {
//            Assertions.assertTrue(e instanceof RuntimeException);
//        }

        //方法2 when有返回值
        when(mockList.get(1)).thenThrow(RuntimeException.class);
        try {
            mockList.get(1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * when(obj.someMethod()).thenXxx();其中obj可以是mock
     * doXxx().when(obj).someMethod();其中obj可以是spy/mock对象,或者无返回值的方法
     */
    @Test
    public void test3(){
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("Tom");
        when(mockUserService.findByUserName("Tom")).thenReturn(user1);
        User userResult1 = mockUserService.findByUserName("Tom");
        System.out.println("userResult1 = " + userResult1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("Jack");
        //spy对象如果不插桩，默认调用真实的方法。如果用when，默认执行一次原来的方法，达不到Mock目的。需要用doReturn
        //when(spyUserService.findByUserName("Jack")).thenReturn(user2);
        doReturn(user2).when(spyUserService).findByUserName("Jack");
        User userResult2 = spyUserService.findByUserName("Jack");
        System.out.println("userResult2 = " + userResult2);


    }

    //void 方法.只能用when
    @Test
    public void test2(){
        doNothing().when(mockList).clear();
        mockList.clear();
        mockList.clear();
        verify(mockList, Mockito.times(2)).clear();
    }

    //指定有返回值的方法，返回值
    @Test
    public void test1(){
        //方法1 doReturn
        doReturn("hello").when(mockList).get(0);
        Assertions.assertEquals("hello", mockList.get(0));

        //方法2 When
        when(mockList.get(1)).thenReturn("world");
        Assertions.assertEquals("world", mockList.get(1));

    }
}
