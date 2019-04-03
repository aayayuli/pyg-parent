package com.pyg.shop.controller;

import bean.PageResult;
import bean.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbGoods;
import com.pyg.sellergoods.service.GoodsService;
import groupEntity.Goods;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }


    /**
     * 返回全部列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int pageNum, int pageSize) {
        return goodsService.findPage(pageNum, pageSize);
    }

    /**
     * 增加
     *
     * @param goods
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getTbGoods().setSellerId(sellerId);
            goodsService.add(goods);
            return new Result(true, "增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "增加失败");
        }
    }

    /**
     * 修改
     *
     * @param goods
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 获取实体
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbGoods findOne(Long id) {
        return goodsService.findOne(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/dele")
    public Result delete(Long[] ids) {
        try {
            goodsService.dele(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 查询+分页
     *
     * @param goods
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbGoods goods, int pageNum, int pageSize) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);
        return goodsService.findPage(goods, pageNum, pageSize);
    }


    @RequestMapping("updateIsMarketable/{ids}/{market}")
    public Result updateIsMarketable(@PathVariable("ids") Long[] ids, @PathVariable("market") String market) {
        try {
            //上下架  1 上架  2 ， 下架
            goodsService.updateIsMarketable(ids, market);
//更新索引库
            //searchService.update()
            //更新静态页面

            //itempageService.update()
            //耦合性太高， 需要使用MQ中间消息件


            return new Result(true, "");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "状态修改失败");
        }

    }

}
