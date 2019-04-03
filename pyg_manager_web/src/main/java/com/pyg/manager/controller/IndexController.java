package com.pyg.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/15  10:13
 * @description TODO
 **/
@RestController
@RequestMapping("index")
public class IndexController {
    @RequestMapping("showName")
    public String showName(){
        //从springSecurity中获取用户名
      String username= SecurityContextHolder.getContext().getAuthentication().getName();
      return  username;
    }
}
