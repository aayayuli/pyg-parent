package com.pyg.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.seckill.service.SeckillService;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/4/3  11:01
 * @description TODO
 **/
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public   List<TbSeckillGoods>  findSeckillGoodsFromRedis() {
        return redisTemplate.boundHashOps("seckill_goods").values();
    }


    @Override
    public TbSeckillGoods findOneFromRedis(Long id) {
     return  (TbSeckillGoods) redisTemplate.boundHashOps("seckill_goods").get(id);

    }
}
