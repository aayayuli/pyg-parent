package com.pinyougou.seckill.task;

import com.pyg.mapper.TbSeckillGoodsMapper;
import com.pyg.pojo.TbSeckillGoods;
import com.pyg.pojo.TbSeckillGoodsExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/4/3  10:15
 * @description TODO
 **/
@Component
public class SeckillTask {
    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Scheduled(cron = "0 15 16  3 4 ?")
    public  void initSeckillData(){
        //从mysql中获取数据

        //        select * from tb_seckill_goods where  STATUS='1'
//        and stock_count>0
//        and  start_time<NOW()
//        and  end_time>NOW()
        TbSeckillGoodsExample example = new TbSeckillGoodsExample();
        example.createCriteria().andStatusEqualTo("1")
                                .andStockCountGreaterThan(0)
                                .andStartTimeLessThan(new Date())
                                .andEndTimeGreaterThan(new Date());
        List<TbSeckillGoods> tbSeckillGoods = seckillGoodsMapper.selectByExample(example);
        for (TbSeckillGoods tbSeckillGood : tbSeckillGoods) {
            Long id = tbSeckillGood.getId();
            redisTemplate.boundHashOps("seckill_goods").put(id,tbSeckillGood);

        }


    }
}
