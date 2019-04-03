package com.pyg.seckill.service;

import com.pyg.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillService {
    List findSeckillGoodsFromRedis();

    TbSeckillGoods findOneFromRedis( Long id);
}
