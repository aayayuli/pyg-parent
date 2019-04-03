package com.pyg.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.mapper.TbOrderItemMapper;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.order.service.OrderService;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderItem;
import com.pyg.pojo.TbPayLog;
import groupEntity.Cart;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/31  21:31
 * @description TODO
 **/
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbPayLogMapper payLogMapper;

    @Override
    public String add(TbOrder order) {//order传递参数  6个参数
//        `payment_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '支付类型，1、微信支付，2、货到付款',
//  `receiver_area_name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人地区名称(省，市，县)街道',
//  `receiver_mobile` varchar(12) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机',
//  `receiver` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
//  `source_type` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '订单来源：1:app端，2：pc端，3
//                userId

        String userId = order.getUserId();
        List<Cart> cartList = (List<Cart>) redisTemplate.boundValueOps("cartList_" + userId).get();

        Long totalFee = 0L;
        String orderList = "";
        for (Cart cart : cartList) {
            TbOrder tbOrder = new TbOrder();
            BeanUtils.copyProperties(order, tbOrder);//tbOrder就有了6个属性值 , 将前端order数据，赋值给new的新order
//  `order_id` bigint(20) NOT NULL COMMENT '订单id',
            long orderId = idWorker.nextId();// 分布式订单id生成方案。
            tbOrder.setOrderId(orderId);
//  `status` varchar(1) COLLATE utf8_bin DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价',
            tbOrder.setStatus("1");
//  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
            tbOrder.setCreateTime(new Date());
//  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
            tbOrder.setUpdateTime(new Date());
//  `seller_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '商家ID',
            tbOrder.setSellerId(cart.getSellerId());
//  `payment` decimal(20,2) DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
            List<TbOrderItem> orderItemList = cart.getOrderItemList();
            BigDecimal payment = new BigDecimal("0");
            for (TbOrderItem orderItem : orderItemList) {
                payment.add(orderItem.getTotalFee());
                orderItem.setId(idWorker.nextId());//主键  TODO 向mysql插入数据时再赋值
                orderItem.setOrderId(orderId);  // TODO 向mysql插入数据时再赋值
                orderItemMapper.insert(orderItem);
            }
            tbOrder.setPayment(payment);
            orderMapper.insert(tbOrder);
            totalFee+=payment.longValue();
            orderList+=orderId+",";
        }
        //合并购物车
        TbPayLog payLog= new TbPayLog();
//         `out_trade_no` varchar(30) NOT NULL COMMENT '支付订单号',
//  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
//  `pay_time` datetime DEFAULT NULL COMMENT '支付完成时间',
//  `total_fee` bigint(20) DEFAULT NULL COMMENT '支付金额（分）',
//  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
//  `transaction_id` varchar(30) DEFAULT NULL COMMENT '交易号码',
//  `trade_state` varchar(1) DEFAULT NULL COMMENT '交易状态',
//  `order_list` varchar(200) DEFAULT NULL COMMENT '订单编号列表',
//  `pay_type` varchar(1) DEFAULT NULL COMMENT '支付类型',
        payLog.setTradeState("0"); //交易状态 0 未交易 1 已交易
//        payLog.setTransactionId();//TODO 交易流水 ，支付成功后微信会返回一个交易的流水号
//        payLog.setPayTime();//TODO 支付后
        payLog.setUserId(userId);
        payLog.setTotalFee(totalFee*100);//  支付金额（分）
        payLog.setPayType(order.getPaymentType()); //'支付类型'
        payLog.setOrderList(orderList.substring(0,orderList.length()-1));
        payLog.setCreateTime(new Date());
        payLog.setOutTradeNo(idWorker.nextId()+"");

        payLogMapper.insert(payLog);
        //        为了能在支付时快速第获取到payLog，我们在这里吧payLog放入到redis中
        redisTemplate.boundHashOps("payLog_"+userId).put(payLog.getOutTradeNo(),payLog);
        //提交订单后清除redis中购物车信息，清空购物车
        redisTemplate.delete("cartList_"+userId);
return payLog.getOutTradeNo();
    }
}
