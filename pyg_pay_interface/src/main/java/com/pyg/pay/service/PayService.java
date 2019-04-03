package com.pyg.pay.service;

import java.util.Map;

public interface PayService {
    Map createNative(String out_trade_no,String userId);

    Map orderQuery(String out_trade_no);

    void updateOrder(String userId, String out_trade_no, String transaction_id);
}
