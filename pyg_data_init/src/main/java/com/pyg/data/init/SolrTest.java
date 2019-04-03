package com.pyg.data.init;

import com.pyg.pojo.TbBrand;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/22  21:12
 * @description TODO
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath:spring/applicationContext-solr.xml")
public class SolrTest {
    @Autowired
    private SolrTemplate solrTemplate;
    @Test
    public void testadd(){
        TbItem tbItem = new TbItem();
        tbItem.setId(100L);
        tbItem.setTitle("springdatesolr测试数据title");
        tbItem.setBrand("测试品牌");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();

    }

    @Test
    public void testupdate(){
        TbItem tbItem = new TbItem();
        tbItem.setId(101L);
        tbItem.setTitle("springdatesolr测试数据title");
        tbItem.setBrand("测试品牌");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();

    }
    @Test
    public  void testquery(){
       Query query = new SimpleQuery("item_title:测试");
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
        List<TbItem> itemList = scoredPage.getContent();
        for (TbItem tbItem : itemList) {
            System.out.println(tbItem.getTitle());
            System.out.println(tbItem.getId());
        }


    }
}
