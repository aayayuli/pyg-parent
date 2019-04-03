package com.pyg.user.controller;

import bean.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone){
        try {
            userService.sendCode(phone);
            return new Result(true,null);
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发送短信失败");
        }
    }
    @RequestMapping("/register/{code}")
    public Result register(@RequestBody TbUser user, @PathVariable("code") String code){
        try {
            userService.register(user,code);
            return new Result(true,null);
        }catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"注册失败");
        }
    }
    @RequestMapping("/showName")
    public String showName(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        return username;
    }
}
