package com.pyg.search.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/26  23:24
 * @description TODO
 **/
public class SolrDeleteConsumer implements MessageListener {
    @Autowired
    private SolrTemplate solrTemplate;
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String goodsId = textMessage.getText();
            SolrDataQuery query=new SimpleQuery("item_goodsid:"+goodsId);
            solrTemplate.delete(query);
            solrTemplate.commit();
            System.out.println("solr is  delete");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
