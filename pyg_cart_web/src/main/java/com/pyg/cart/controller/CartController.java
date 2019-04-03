package com.pyg.cart.controller;

import bean.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.cart.service.CartSerive;
import groupEntity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.CookieUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date  2019/3/30  9:33
 * @description
 **/
@RestController
@RequestMapping("cart")
public class CartController {
    @Reference
    private CartSerive cartSerive;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    public String getUUID(){
        String uuid = CookieUtil.getCookieValue(request, "user-key");
        if(uuid==null){
            uuid = UUID.randomUUID().toString();
            CookieUtil.setCookie(request,response,"user-key",uuid,60*60*24*7);
        }
        return uuid;
    }
    @RequestMapping("findCartList")
    public List<Cart> findCartList() {
        String uuid = getUUID();
        List<Cart> cartList_uuid = cartSerive.findCartListFromRedisByKey(uuid);

        //两份购物车的数据合并  把合并的存入redis  清除cartList_uuid
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userId.equals("anonymousUser")) {//意味着登录  anonymousUser是security提供的一个默认
            List<Cart> cartList_userid = cartSerive.findCartListFromRedisByKey(userId);//获取购物车数据
            if (cartList_uuid.size() > 0) {//判断是否需要合并
//                合并
                cartList_userid = cartSerive.mergeCartList(cartList_uuid, cartList_userid);
//                保存
                cartSerive.saveCartListToRedis(userId, cartList_userid,true);
//                从Redis中 清除cartList_uuid
                cartSerive.deleteCartListFromRedisByKey(uuid);
            }
            return cartList_userid;

        }
        return cartList_uuid;
    }

    @RequestMapping("addGoodsToCartList")
//    @CrossOrigin(origins = {"http://item.pinyougou.com","http://www.pinyougou.com"})
    @CrossOrigin("*")
    public Result addGoodsToCartList(Long itemId, int num) {
        try {
            List<Cart> cartList = findCartList();//获取 原购物车的数据
            cartList = cartSerive.addGoodsToCartList(cartList, itemId, num);//向原购物车追加新商品后 形成新购物车
            //重新存入Redis
            String uuid = getUUID();
            //            未登录时也可以从security中获取用户名："anonymousUser"
//            判断是否有当前登录人
            String user_Id = SecurityContextHolder.getContext().getAuthentication().getName();
            if (user_Id.equals("anonymousUser")) {//意味着weidengl
                cartSerive.saveCartListToRedis(uuid, cartList,false);//新的购物车存入
            } else {//登录
                cartSerive.saveCartListToRedis(user_Id, cartList,true);//新的购物车存入

            }
            return new Result(true, "");
        } catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"购物车添加失败");
        }
    }

    //itemId   num

}
