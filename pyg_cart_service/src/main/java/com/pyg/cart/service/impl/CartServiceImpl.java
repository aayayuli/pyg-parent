package com.pyg.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.cart.service.CartSerive;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbOrderItem;
import groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/30  10:42
 * @description TODO
 **/
@Service
public class CartServiceImpl implements CartSerive {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public List<Cart> findCartListFromRedisByKey(String userKey) {
//        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(uuid);
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cartList_"+userKey).get();
        if (cartList == null) {
            cartList = new ArrayList<>();//不直接return null
        }
        return cartList;
    }

    @Override
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, int num) {
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        if (tbItem == null) {
            throw new RuntimeException("无此商品");
        }
        String sellerId = tbItem.getSellerId();
        Cart cart = findCartFromCartListBySellerId(cartList, sellerId);
        //        判断即将添加的商品所属的商家有没有对应的购物车对象cart
        if (cart != null) {  // 如果有：
//             还需要判断即将添加的商品有没有出现在此购物车中
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            TbOrderItem tbOrderItem = findTbOrderItemFromOrderItemListByItemId(orderItemList, itemId);
            if (tbOrderItem != null) {
                //              如果有：数据累加，小计金额重新计算
                tbOrderItem.setNum(tbOrderItem.getNum() + num);
//                BigDecimal的相乘  multiply
                BigDecimal totalFee = tbOrderItem.getPrice().multiply(new BigDecimal(tbOrderItem.getNum()));
                tbOrderItem.setTotalFee(totalFee);

                if (tbOrderItem.getNum() == 0) {//此商品数量为零  应该从orderItemList中移除
                    orderItemList.remove(tbOrderItem);
                    //还需判断此商家下是否还有  商品数据
                    if (orderItemList.size() == 0) {
                        //意味着此商家没有商品了  应该把此商家对应的cart对象从cartList中移除
                        cartList.remove(cart);
                    }
                }
            } else {
                //  如果没有：重新创建TbOrderItem对象，
                tbOrderItem = new TbOrderItem();
                tbOrderItem = createTbOrderItem(tbOrderItem, tbItem, num);
//                放入到orderItem
                orderItemList.add(tbOrderItem);
            }
        } else {
            //如果没有 ：创建新的cart对象， cart放入cartList
            cart = new Cart();
            cart.setSellerId(tbItem.getSellerId());
            cart.setSellerName(tbItem.getSeller());
            List<TbOrderItem> orderItemList = new ArrayList<>();

            TbOrderItem tbOrderItem = new TbOrderItem();
            tbOrderItem = createTbOrderItem(tbOrderItem, tbItem, num);
            orderItemList.add(tbOrderItem);
            cart.setOrderItemList(orderItemList);
            cartList.add(cart);
        }
        return cartList;
    }


    private TbOrderItem createTbOrderItem(TbOrderItem tbOrderItem, TbItem tbItem, int num) {
        tbOrderItem.setNum(num);
        tbOrderItem.setPrice(tbItem.getPrice());
        tbOrderItem.setTotalFee(tbOrderItem.getPrice().multiply(new BigDecimal(tbOrderItem.getNum())));
        //                 tbOrderItem.setId();//主键  TODO 向mysql插入数据时再赋值
//                 tbOrderItem.setOrderId();  // TODO 向mysql插入数据时再赋值
        tbOrderItem.setSellerId(tbItem.getSellerId());
        tbOrderItem.setTitle(tbItem.getTitle());

        tbOrderItem.setPicPath(tbItem.getImage());
        tbOrderItem.setItemId(tbItem.getId());
        tbOrderItem.setGoodsId(tbItem.getGoodsId());
        return tbOrderItem;
    }


    private TbOrderItem findTbOrderItemFromOrderItemListByItemId(List<TbOrderItem> orderItemList, Long itemId) {
        for (TbOrderItem tbOrderItem : orderItemList) {
            if (tbOrderItem.getItemId().longValue() == itemId.longValue()) {
                return tbOrderItem;
            }
        }
        return null;

    }

    private Cart findCartFromCartListBySellerId(List<Cart> cartList, String sellerId) {
        for (Cart cart : cartList) {
            if (cart.getSellerId().equals(sellerId)) {
                return cart;
            }
        }
        return null;
    }

    @Override
    public void saveCartListToRedis(String userKey, List<Cart> cartList,Boolean isUserId) {
        //如果是userID 时效性6个月， 如果不是 时效性两天
//        redisTemplate.boundHashOps("cartList").put(uuid,cartList);
        if (isUserId){
            redisTemplate.boundValueOps("cartList_"+userKey).set(cartList,180, TimeUnit.DAYS);
        }else {
            redisTemplate.boundValueOps("cartList_"+userKey).set(cartList,2, TimeUnit.DAYS);

        }
    }

    @Override
    public void deleteCartListFromRedisByKey(String userKey) {
//        redisTemplate.boundHashOps("cartList").delete(uuid);
        redisTemplate.delete("cartList_"+userKey);
    }


    @Override
    public List<Cart> mergeCartList(List<Cart> cartList_uuid, List<Cart> cartList_userid) {
//        cartList_uuid合并到cartList_userId上
        for (Cart cart : cartList_uuid) {
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            for (TbOrderItem orderItem : orderItemList) {
                 cartList_userid = addGoodsToCartList(cartList_userid, orderItem.getItemId(), orderItem.getNum());
            }
        }
        return cartList_userid;
    }

}
