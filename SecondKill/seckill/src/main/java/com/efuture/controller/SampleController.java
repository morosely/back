package com.efuture.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efuture.domain.User;
import com.efuture.redis.RedisService;
import com.efuture.redis.UserKey;
import com.efuture.result.CodeMsg;
import com.efuture.result.Result;
import com.efuture.service.UserService;

@Controller
@RequestMapping("/simple")
public class SampleController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }
	
	@RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
 		return Result.success("Hello,Spring Boot");
       // return new Result(0, "success", "hello,imooc");
    }
 	
 	@RequestMapping("/error")
    @ResponseBody
    public Result<String> helloError() {
 		return Result.error(CodeMsg.SERVER_ERROR);
 		//return new Result(500102, "XXX");
    }
 	
 	@RequestMapping("/thymeleaf")
    public String  thymeleaf(Model model) {
 		model.addAttribute("name", "Spring Boot");
 		return "hello";
    }
 	
 	 @RequestMapping("/db/getById")
     @ResponseBody
     public Result<User> getById() {
     	User user = userService.getById(1l);
         return Result.success(user);
     }
     
     
     @RequestMapping("/db/tx")
     @ResponseBody
     public Result<Boolean> dbTx() {
     	//userService.tx();
        return Result.success(true);
     }
     
     @RequestMapping("/redis/get")
     @ResponseBody
     public Result<User> redisGet() {
     	User user = redisService.get(UserKey.ID, ""+1, User.class);
         return Result.success(user);
     }
     
     @RequestMapping("/redis/set")
     @ResponseBody
     public Result<Boolean> redisSet() {
     	User user  = new User();
     	user.setId(1l);
     	user.setNickName("Java");
     	redisService.set(UserKey.ID, ""+1, user);//UserKey:id1
         return Result.success(true);
     }
}
