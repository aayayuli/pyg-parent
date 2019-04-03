package com.pyg.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.order.service.AddressSerice;
import com.pyg.pojo.TbAddress;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/31  11:29
 * @description TODO
 **/
@RestController
@RequestMapping("address")
public class AddressController {
    @Reference
    private AddressSerice addressSerice;

    @RequestMapping("findAddressListByUserId")
    public List<TbAddress> findAddressListByUserId() {
//        springSecurity是web层框架， 只能在web层获取用户名
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return addressSerice.findAddressListByUserId(userId);
    }
}
