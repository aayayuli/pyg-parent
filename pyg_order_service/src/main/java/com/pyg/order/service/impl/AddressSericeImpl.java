package com.pyg.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbAddressMapper;
import com.pyg.order.service.AddressSerice;
import com.pyg.pojo.TbAddress;
import com.pyg.pojo.TbAddressExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/31  11:35
 * @description TODO
 **/
@Service
public class AddressSericeImpl implements AddressSerice {
    @Autowired
    private TbAddressMapper addressMapper;
    @Override
    public List<TbAddress> findAddressListByUserId(String userId) {
//        select*from tb_address where userid=?
        TbAddressExample example = new TbAddressExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return addressMapper.selectByExample(example);
    }
}
