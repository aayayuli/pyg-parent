package com.pyg.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pay.service.PayService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/4/1  10:18
 * @description TODO
 **/
@RestController
@RequestMapping("pay")
public class PayController {
    @Reference
    private PayService payService;

    @RequestMapping("createNative")
    public Map createNative(String out_trade_no) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return payService.createNative(out_trade_no, userId);
    }
    @RequestMapping("/orderQuery")
    public Map orderQuery(String out_trade_no){
        return payService.orderQuery(out_trade_no);
    }

    @RequestMapping("/updateOrder")
    public void updateOrder(String out_trade_no,String transaction_id){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        payService.updateOrder(userId,out_trade_no,transaction_id);

    }
}
