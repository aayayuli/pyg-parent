package com.pyg.itempage.mq;

import com.pyg.itempage.service.ItempageService;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/26  22:58
 * @description TODO
 **/
public class itempageDeleteConsumer implements MessageListener {
 @Autowired
 private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage= (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            TbItemExample  example =new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = itemMapper.selectByExample(example);
            for (TbItem tbItem : itemList) {
                new File("D://Test//class61freemarker//"+tbItem.getId()+".html").delete();
            }
            System.out.println("itempage is delete");
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
