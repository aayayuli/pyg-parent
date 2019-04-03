package com.pyg.cart.service;

import groupEntity.Cart;

import java.util.List;

public interface CartSerive {
    List<Cart> findCartListFromRedisByKey(String userKey);

    List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, int num);

    void saveCartListToRedis(String userKey, List<Cart> cartList,Boolean isUserId);

    void deleteCartListFromRedisByKey(String uuid);

    List<Cart> mergeCartList(List<Cart> cartList_uuid, List<Cart> cartList_userid);


}
