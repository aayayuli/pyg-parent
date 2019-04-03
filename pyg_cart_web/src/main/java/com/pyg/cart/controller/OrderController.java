package com.pyg.cart.controller;

import bean.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.TbOrder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/31  21:25
 * @description TODO
 **/
@RestController
@RequestMapping("order")
public class OrderController {
    @Reference
    private OrderService orderServic;
    @RequestMapping("add")
    public Result add(@RequestBody TbOrder order){
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            order.setUserId(userId);
           String out_trade_no =orderServic.add(order);//下单成功后吧合并后的交易订单号返回给界面
            return  new Result(true,out_trade_no);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"订单提交失败");
        }
    }
}
