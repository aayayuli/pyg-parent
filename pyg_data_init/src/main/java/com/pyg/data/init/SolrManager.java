package com.pyg.data.init;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemMapper;
import com.pyg.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/22  21:35
 * @description TODO
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath*:spring/applicationContext*.xml")
public class SolrManager {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;
    @Test
    public  void  initSolrData(){
        //从mysql中查询数据， 查询所有上架的sku数据
//        SELECT i.* FROM tb_item i, tb_goods g
//        WHERE i.goods_id=g.id
//        AND g.is_marketable='1'

        List<TbItem> itemList= itemMapper.selectGrounding();
        for (TbItem tbItem : itemList) {
            String spec = tbItem.getSpec();//{"水杯材质"："玻璃"，"水杯容量":"800ml"}
            Map specMap = JSON.parseObject(spec,Map.class);
            tbItem.setSpecMap(specMap);
        }
        //把数据放入solr
         solrTemplate.saveBeans(itemList);
         solrTemplate.commit();

    }
}
