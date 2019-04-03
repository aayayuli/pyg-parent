package com.pyg.search.mq;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import com.pyg.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/26  23:24
 * @description TODO
 **/
public class SolrUpdateConsumer implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            TbItemExample example = new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = itemMapper.selectByExample(example);

            for (TbItem tbItem : itemList) {
                String spec = tbItem.getSpec();
                Map specMap = JSON.parseObject(spec, Map.class);//{"水杯材质"："玻璃"，"水杯容量":"800ml"}
                tbItem.setSpecMap(specMap);
            }
             //根据spuid查询sku列表
            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();
            System.out.println("solr is  update");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
