package com.pyg.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.seckill.service.SeckillService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/4/3  10:58
 * @description TODO
 **/
@RestController
@RequestMapping("seckill")
public class SeckillController {
    @Reference
    private SeckillService seckillService;

    @RequestMapping("findSeckillGoods")
    public List<TbSeckillGoods> findSeckillGoods() {
    return seckillService.findSeckillGoodsFromRedis();
    }

    @RequestMapping("findOne/{id}")
    public TbSeckillGoods findOne(@PathVariable("id") Long id) {
        return seckillService.findOneFromRedis(id);
    }
}
