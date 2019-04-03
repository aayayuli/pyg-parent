package com.pyg.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pyg.pojo.TbItem;
import com.pyg.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/23  9:43
 * @description TODO
 **/
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map searchByParamMap(Map paramMap) {
        Map resultMap = new HashMap<>();
        //根据关键字查询 paramMap :   {keyword:"小米"}
        // Query query=new SimpleQuery("item_title:"+paramMap.get("keyword"));
        // ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);


        //-------------------分组查询开始----------------------------
        //select category from tb_item where title like '% 三星 %' group by category
        Query groupQuery = new SimpleQuery(new Criteria("item_keywords").is(paramMap.get("keyword")));//相当于from tb_item where title like '% 三星 %'
        //指定要分组的域名
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");//相当于 group by category
        groupQuery.setGroupOptions(groupOptions);
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(groupQuery, TbItem.class);
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");//select category
        List<GroupEntry<TbItem>> groupEntryList = groupResult.getGroupEntries().getContent();
        List<String> categoryList = new ArrayList<>();
        for (GroupEntry<TbItem> tbItemGroupEntry : groupEntryList) {
            categoryList.add(tbItemGroupEntry.getGroupValue());
        }
        resultMap.put("categoryList", categoryList);

        //从第一个分类中获取品牌和规格数据
        if (categoryList.size() > 0) {
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("category_brand").get(categoryList.get(0));//categoryList.get(0)即categoryName
            List<Map> specList = (List<Map>) redisTemplate.boundHashOps("category_spec").get(categoryList.get(0));
            resultMap.put("brandList",brandList);
            resultMap.put("specList",specList);
        }

        //-------------------分组查询结束----------------------------


        //-------------------高亮查询开始----------------------------
        HighlightQuery highlightQuery = new SimpleHighlightQuery
                (new Criteria("item_keywords").is(paramMap.get("keyword")));
        //设置高亮相关属性
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");//那个域
        //使用什么标签包裹关键字
        highlightOptions.setSimplePrefix("<span style=\"color:red\">");
        highlightOptions.setSimplePostfix("</span>");
        highlightQuery.setHighlightOptions(highlightOptions);
        //添加过滤查询 paramMap={"keyword":"小米","category":"手机","brand":"三星","spec":{"网络":"联通4G"},"price":"1000-1500"}
        //在主查询的基础上添加过滤查询
        //分类
        if(!paramMap.get("category").equals("")){
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_category").is(paramMap.get("category"))));
        }//品牌
        if(!paramMap.get("brand").equals("")){
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_brand").is(paramMap.get("brand"))));
        }
        //规格    "spec":{"网络":"联通4G"，"机身内存"："16G","手机屏幕尺寸":"5寸"}
         Map specMap = (Map) paramMap.get("spec");
        for (Object key : specMap.keySet()) {
            highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_"+key).is(paramMap.get("key"))));
        }
       // highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_机身内存").is("16G")));
       // highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_spec_手机屏幕尺寸").is("5寸")));
        //价格   "price":"1000-1500"}
        if(!paramMap.get("price").equals("")){
            String[] prices = paramMap.get("price").toString().split("-");
            if (!prices[1].equals("*")){
                highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_price").between(prices[0],prices[1],true,true)));
            }else {
                highlightQuery.addFilterQuery(new SimpleQuery(new Criteria("item_price").greaterThanEqual(prices[0])));
            }
        }
        //按照价格排序
        if (paramMap.get("order").equals("asc")) {
            highlightQuery.addSort(new Sort(Sort.Direction.ASC, "item_price"));
        }else {
            highlightQuery.addSort(new Sort(Sort.Direction.DESC, "item_price"));

        }
        // 分页数据传入......起始位置=()*每页显示的条数
        Integer pageNum = (Integer) paramMap.get("pageNum");
        highlightQuery.setRows(60);//每页显示的条数
        highlightQuery.setOffset(pageNum-1*60);//起始位置

        HighlightPage<TbItem> highlightPage = solrTemplate.queryForHighlightPage(highlightQuery, TbItem.class);
        //将上highlightPage 在测试类中转JSON打印出 分析写出如下
        List<TbItem> itemList = highlightPage.getContent();

        for (TbItem tbItem : itemList) {
            List<HighlightEntry.Highlight> highlights = highlightPage.getHighlights(tbItem);
            if (highlights != null && highlights.size() > 0) {
                HighlightEntry.Highlight highlight = highlights.get(0);
                List<String> snipplets = highlight.getSnipplets();
                if (snipplets != null && snipplets.size() > 0) {
                    String title = snipplets.get(0);
                    tbItem.setTitle(title);
                }
            }
        }
        //-------------------高亮查询结束----------------------------


        resultMap.put("total", highlightPage.getTotalElements()); // 总条数
        resultMap.put("totalPages", highlightPage.getTotalPages()); // 当前页码数
        resultMap.put("itemList", highlightPage.getContent()); // 当前页数据
        return resultMap;
    }
}
