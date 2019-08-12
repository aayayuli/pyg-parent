package com.pyg.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbUserMapper;
import com.pyg.pojo.TbUser;
import com.pyg.user.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/28  9:40
 * @description TODO
 **/
@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbUserMapper userMapper;
    @Override
    public void sendCode(String phone) {
        //判断手机号发送了几次  （同一个电话号码一个小时内最多三次）
        Object count = redisTemplate.boundValueOps("sms_count" + phone).get();
        if (count==null){//说明第一次发送
            redisTemplate.boundValueOps("sms_count" + phone).set(1,1, HOURS);
        }else {
            //发一次记录一次
            Integer smsCount = (Integer) count;
            if (smsCount>=50){
               throw new RuntimeException("此号码发送频繁，请一个小时候再试");
           }else {
               redisTemplate.boundValueOps("sms_count" + phone).set(smsCount+1,1, HOURS);
           }
        }



        Map<String,String> map = new HashMap<>();
        map.put("phoneNumbers",phone);
        map.put("signName","文王通讯");
        map.put("templateCode","SMS_162115705");
        String numeric = RandomStringUtils.randomNumeric(4);
        map.put("templateParam","{\"code\":\""+numeric+"\"}");
        System.out.println(numeric);
        jmsTemplate.convertAndSend("pyg_sms",map);

//        redisTemplate.boundValueOps("sms_"+phone).set(s,5,TimeUnit.MINUTES);
        redisTemplate.boundValueOps("sms_"+phone).set(numeric,30,TimeUnit.SECONDS);//30秒
    }


    @Override
    public void register(TbUser user, String code) {
        //todo 按照手机号码和用户名判断是否有重复
        //user.getusername(),user.getPhone();
        //select*from tb _user where username = ?  or phong = ?



//        1.校验验证码，
        String numeric = (String) redisTemplate.boundValueOps("sms_" + user.getPhone()).get();
        if (numeric==null){
            throw new RuntimeException("验证码已失效");
        }
        if (!code.equals(numeric)){
            throw  new RuntimeException("验证码输入错误");
        }
//        2.向MYsql 插入
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        userMapper.insert(user);
        System.out.println(userMapper);
    }
}
