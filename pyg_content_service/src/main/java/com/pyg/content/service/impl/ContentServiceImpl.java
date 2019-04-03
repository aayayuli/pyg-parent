package com.pyg.content.service.impl;

import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.content.service.ContentService;
import com.pyg.mapper.TbContentMapper;
import com.pyg.pojo.TbContent;
import com.pyg.pojo.TbContentExample;
import com.pyg.pojo.TbContentExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //解决页面数据 修改后,数据从redis来, 前端显示修改前数据的问题
        redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
    }



    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //首页轮播--今日推荐
        TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
//        tbContent.getCategoryId();//首页轮播类型
        contentMapper.updateByPrimaryKey(content);
        //解决页面数据 修改后,数据从redis来, 前端显示修改前数据的问题
        redisTemplate.boundHashOps("contentList").delete(tbContent.getCategoryId());//删除的是首页轮播
        redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());//删除的是今日推荐
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void dele(Long[] ids) {
        for (Long id : ids) {
            //解决页面数据 修改后,数据从redis来, 前端显示修改前数据的问题
            TbContent content = contentMapper.selectByPrimaryKey(id);
            redisTemplate.boundHashOps("contentList").delete(content.getCategoryId());
            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {

        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("contentList").get(categoryId);
        if (contentList == null) {//没有数据  从mysql中拿
            // select*from tb_content where categoryId=? and status='1' order by sort_order desc
            TbContentExample example = new TbContentExample();
            example.createCriteria().andStatusEqualTo("1").andCategoryIdEqualTo(categoryId);
            example.setOrderByClause("sort_order desc");
            contentList = contentMapper.selectByExample(example);
            redisTemplate.boundHashOps("contentList").put(categoryId,contentList);
            System.out.println("mysql 中来");
        }
        else {
            System.out.println("redis中来");
        }
        return contentList;

    }
}
