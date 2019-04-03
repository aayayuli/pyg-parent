package com.pyg.data.init;

import com.alibaba.fastjson.JSON;
import com.pyg.mapper.TbItemCatMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.mapper.TbTypeTemplateMapper;
import com.pyg.pojo.TbItemCat;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.pojo.TbTypeTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/24  9:48
 * @description TODO
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class ReidsManager {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbTypeTemplateMapper typeTemplateMapper;
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;

    //把所有分类名称对应的品牌和规格都初始化到redis当中
    //tb_item_cat   tb_type_template  tb_specification_option
    @Test
    public  void initRedis(){
        List<TbItemCat> itemCatList = itemCatMapper.selectByExample(null);
        for (TbItemCat tbItemCat : itemCatList) {
            TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(tbItemCat.getTypeId());
            String brandIds = tbTypeTemplate.getBrandIds();//   需要的格式 brandList  [{"id":1,"text":"联想"},{"id":3,"text":"三星"}]
            List<Map> brandList = JSON.parseArray(brandIds, Map.class);
            redisTemplate.boundHashOps("category_brand").put(tbItemCat.getName(),brandList);
            String specIds = tbTypeTemplate.getSpecIds();// 需要拼凑的格式 [{"id":27,"text":"网络"，options[{},{},{}]},{"id":32,"text":"机身内存"}]
            List<Map> specList = JSON.parseArray(specIds, Map.class);
            for (Map map : specList) {
                List<TbSpecificationOption> options = specificationOptionMapper.selectBySpecId(Long.parseLong(map.get("id").toString()));
                map.put("options",options);
            }
            redisTemplate.boundHashOps("category_spec").put(tbItemCat.getName(),specList);
        }
        System.out.println("redis  init is OK");
    }

}
