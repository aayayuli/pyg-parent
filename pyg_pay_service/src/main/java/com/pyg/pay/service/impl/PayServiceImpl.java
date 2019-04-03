package com.pyg.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pyg.mapper.TbOrderMapper;
import com.pyg.mapper.TbPayLogMapper;
import com.pyg.pay.service.PayService;
import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbPayLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import utils.HttpClient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/4/1  10:21
 * @description 订单向微信平台付款的功能
 **/
@Service
public class PayServiceImpl implements PayService {

    @Value("${appid}")
    private String appid;  //公众号id
    @Value("${partner}")
    private String partner; // 商户号
    @Value("${partnerkey}")
    private String partnerkey;//商户号密码

    @Value("${notifyurl}")
    private String notifyurl; //回调地址 工信部备案的域名并且不能带参数

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbPayLogMapper payLogMapper;
    @Autowired
    private TbOrderMapper orderMapper;

    @Override
    public Map createNative(String out_trade_no, String userId) {
        //调用微信统一下载api
        // https://api.mch.weixin.qq.com/pay/unifiedorder
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

        try {
            TbPayLog PayLog = (TbPayLog) redisTemplate.boundHashOps("payLog_" + userId).get(out_trade_no);
            Map<String, String> paramMap = new HashMap<>();
            //        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
            paramMap.put("mch_id", partner);
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
//        商品描述	body	是	String(128)	腾讯充值中心-QQ会员充值 商品简单描述，该字段请按照规范传递，具体请见参数规定
            paramMap.put("body", "品优购支付");
//        商户订单号	out_trade_no	是	String(32)	20150806125346
            paramMap.put("out_trade_no", out_trade_no);
//        标价金额	total_fee	是	Int	88	订单总金额，单位为分，详见支付金额
//        paramMap.put("total_fee",payLog.getTotalFee().toString()); //TODO 正式运行时 用这行代码
            paramMap.put("total_fee", "1");
//        终端IP	spbill_create_ip	是	String(64)	123.12.12.123	支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
            paramMap.put("spbill_create_ip", "127.0.0.1");
//        通知地址	notify_url	是	String(256)	http://www.weixin.qq.com/wxpay/pay.php	异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            paramMap.put("notify_url", notifyurl);
//        交易类型	trade_type	是	String(16)	JSAPI NATIVE
            paramMap.put("trade_type", "NATIVE");
            //        签名	sign	是	String(32)	C380BEC2BFD727A4B6845133519F3AD6	通过签名算法计算得出的签名值，详见签名生成算法
//                我们组装Map 使用微信提供的工具类转成XML
//        WXPayUtil.mapToXml(paramMap);
            String signedXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);//生成带有签名的xml

            //            如果调用 远程调用技术：
            httpClient.setXmlParam(signedXml);//设置参数
            httpClient.post();// 发送请求
            String content = httpClient.getContent();//获取结果， 是XML格式的
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);//XML  转map
            resultMap.put("totalFee", PayLog.getTotalFee().toString());
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Map orderQuery(String out_trade_no) {
        //        调用微信的查询订单支付状态的API
//       https://api.mch.weixin.qq.com/pay/orderquery

        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        Map<String, String> paramMap = new HashMap<>();

        try {
            //        公众账号ID	appid	是	String(32)	wxd678efh567hg6787	微信支付分配的公众账号ID（企业号corpid即为此appId）
            paramMap.put("appid", appid);
//        商户号	mch_id	是	String(32)	1230000109	微信支付分配的商户号
            paramMap.put("mch_id", partner);
//        随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，长度要求在32位以内。推荐随机数生成算法
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
//        商户订单号	out_trade_no	是	String(32)	20150806125346
            paramMap.put("out_trade_no", out_trade_no);
            String signedXml = WXPayUtil.generateSignedXml(paramMap, partnerkey);


//            如果调用 远程调用技术：
            httpClient.setXmlParam(signedXml); //设置参数
            httpClient.post();   //发送请求
            String content = httpClient.getContent(); //获取结果 是xml格式的
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content); //把xml格式转成map
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两张表的修改
     *
     * @param userId
     * @param out_trade_no
     * @param transaction_id
     */
    @Override
    public void updateOrder(String userId, String out_trade_no, String transaction_id) {
        TbPayLog payLog = (TbPayLog) redisTemplate.boundHashOps("payLog_" + userId).get(out_trade_no);
        //        tb_pay_log
//        pay_time  new Date()
//        transaction_id   前端
//        trade_state  1
        payLog.setPayTime(new Date());
        payLog.setTransactionId(transaction_id);
        payLog.setTradeState("1");
        payLogMapper.updateByPrimaryKey(payLog);



        //        tb_order
//        status  2
//        update_time  new Date()
//        payment_time  new Date()
        String[] orderIds = payLog.getOrderList().split(",");//1111938436756967424,1111938436773744640
        for (String orderId : orderIds) {
            TbOrder tbOrder = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
            tbOrder.setStatus("2");
            tbOrder.setUpdateTime(new Date());
            tbOrder.setPaymentTime(new Date());
            orderMapper.updateByPrimaryKey(tbOrder);
        }
        redisTemplate.boundHashOps("payLog_"+userId).delete(out_trade_no);//删除待支付的订单

    }
}
