package com.efuture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.efuture.domain.User;
import com.efuture.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping(value="/info")
	@ResponseBody
    public Result<User> list(Model model,User user) {
    	model.addAttribute("user", user);
    	System.out.println("user ========== >>> "+ user);
    	return Result.success(user);
    }
}
