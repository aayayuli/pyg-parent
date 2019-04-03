package com.pyg.itempage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.itempage.service.ItempageService;
import com.pyg.mapper.TbGoodsDescMapper;
import com.pyg.mapper.TbGoodsMapper;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import com.sun.org.apache.bcel.internal.generic.IRETURN;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/25  22:40
 * @description TODO
 **/
@Service
public class ItempageServiceImpl implements ItempageService {
    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Override
    public Goods findOne(Long goodsId) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(goodsId);
        goods.setTbGoods(tbGoods);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setTbGoodsDesc(tbGoodsDesc);

//        select*from tb_item where goodsid= ?
        TbItemExample example= new TbItemExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);
        return goods;
    }

    /**
     * 生成全部静态页面
     * @param goodsId
     * @return
     */
    @Override
    public List<Goods> findAll(Long goodsId) {
        List<Goods> goodsList=new ArrayList<>();
        //获取所有组合类
        List<TbGoods> tbGoodsList = goodsMapper.selectByExample(null);
        for (TbGoods tbGoods : tbGoodsList) {
            Goods goods = findOne(tbGoods.getId());
            goodsList.add(goods);
        }
        return goodsList;
    }
}
