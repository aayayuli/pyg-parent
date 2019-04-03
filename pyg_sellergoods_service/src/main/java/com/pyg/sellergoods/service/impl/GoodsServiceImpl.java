package com.pyg.sellergoods.service.impl;

import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.*;
import com.pyg.pojo.TbGoods;
import com.pyg.pojo.TbGoodsDesc;
import com.pyg.pojo.TbGoodsExample;
import com.pyg.pojo.TbGoodsExample.Criteria;
import com.pyg.pojo.TbItem;
import com.pyg.sellergoods.service.GoodsService;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.*;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper GoodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbBrandMapper brandMapper;
    @Autowired
    private TbSellerMapper sellerMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbGoods> findAll() {
        return goodsMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(Goods goods) {
        TbGoods tbGoods = goods.getTbGoods();
        tbGoods.setAuditStatus("0");
        tbGoods.setIsMarketable("0");
        tbGoods.setIsDelete("0");
        goodsMapper.insert(tbGoods);//插入成功后返回主键ID


        TbGoodsDesc tbGoodsDesc = goods.getTbGoodsDesc();
        tbGoodsDesc.setGoodsId(tbGoods.getId());
        GoodsDescMapper.insert(tbGoodsDesc);


        //sku数据
        List<TbItem> itemList = goods.getItemList();
        for (TbItem tbItem : itemList) {


            //  `spec` varchar(200) DEFAULT NULL,  "spec":{"水杯材质":"玻璃","水杯容量":"1000ml"}
            //  `title` varchar(100) NOT NULL COMMENT '商品标题',  大水杯 玻璃 1000ml
            String title = tbGoods.getGoodsName();//大水杯
            String spec = tbItem.getSpec();// {"水杯材质":"玻璃","水杯容量":"1000ml"}
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);
            for (String key : specMap.keySet()) {
                title += "" + specMap.get(key);
            }
            tbItem.setTitle(title);
            //  `sell_point` varchar(500) DEFAULT NULL COMMENT '商品卖点',  //取自spu的 副标题
            tbItem.setSellPoint(tbGoods.getCaption());
            //  `image` varchar(2000) DEFAULT NULL COMMENT '商品图片', //取spu的第一张图片地址
            String itemImages = tbGoodsDesc.getItemImages();// [{color:"",url:''},{}]
            List<Map> imageMapList = JSON.parseArray(itemImages, Map.class);
            if (imageMapList != null && imageMapList.size() >= 0) {
                String image = imageMapList.get(0).get("url").toString();
                tbItem.setImage(image);
            }
            //  `categoryId` bigint(10) NOT NULL COMMENT '所属类目，叶子类目',  //取spu的第三级分类id
            tbItem.setCategoryid(tbGoods.getCategory3Id());
            //  `status` varchar(1) NOT NULL COMMENT '商品状态，1-正常，2-下架，3-删除',  ///1-正常
            tbItem.setStatus("1");
            //  `create_time` datetime NOT NULL COMMENT '创建时间',  new Date()
            //  `update_time` datetime NOT NULL COMMENT '更新时间',  new Date()
            tbItem.setCreateTime(new Date());
            tbItem.setUpdateTime(new Date());
            //  `goods_id` bigint(20) DEFAULT NULL,      spuId
            tbItem.setGoodsId(tbGoods.getId());
            //  `seller_id` varchar(30) DEFAULT NULL,    从tbGoods中获取
            tbItem.setSellerId(tbGoods.getSellerId());
            //  `category` varchar(200) DEFAULT NULL,  分类名称
            tbItem.setCategory(itemCatMapper.selectByPrimaryKey(tbItem.getCategoryid()).getName());
            //  `brand` varchar(100) DEFAULT NULL,     品牌名称
            tbItem.setBrand(brandMapper.selectByKey(tbGoods.getBrandId()).getName());
            //  `seller` varchar(200) DEFAULT NULL,    商家的名称
            tbItem.setSeller(sellerMapper.selectByPrimaryKey(tbGoods.getSellerId()).getName());


            itemMapper.insert(tbItem);

        }


    }


    /**
     * 修改
     */
    @Override
    public void update(TbGoods goods) {
        goodsMapper.updateByPrimaryKey(goods);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbGoods findOne(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void dele(Long[] ids) {
        for (Long id : ids) {
            goodsMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example = new TbGoodsExample();
        Criteria criteria = example.createCriteria();

        if (goods != null) {
            if (goods.getSellerId() != null && goods.getSellerId().length() > 0) {
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
                criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
            }
            if (goods.getAuditStatus() != null && goods.getAuditStatus().length() > 0) {
                criteria.andAuditStatusEqualTo(goods.getAuditStatus());
            }
            if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
                criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
            }
            if (goods.getCaption() != null && goods.getCaption().length() > 0) {
                criteria.andCaptionLike("%" + goods.getCaption() + "%");
            }
            if (goods.getSmallPic() != null && goods.getSmallPic().length() > 0) {
                criteria.andSmallPicLike("%" + goods.getSmallPic() + "%");
            }
            if (goods.getIsEnableSpec() != null && goods.getIsEnableSpec().length() > 0) {
                criteria.andIsEnableSpecLike("%" + goods.getIsEnableSpec() + "%");
            }
            if (goods.getIsDelete() != null && goods.getIsDelete().length() > 0) {
                criteria.andIsDeleteLike("%" + goods.getIsDelete() + "%");
            }

        }

        Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateAuditStatus(String status, Long[] ids) {
        Map<String, Object> map = new HashMap<>();//优化
        map.put("status", status);
        for (Long id : ids) {
            //TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
            //tbGoods.setAuditStatus(status);
            //goodsMapper.updateByPrimaryKey(tbGoods);
            //todo  sql优化
            map.put("sellerId", id);
            goodsMapper.updateStatusById(map);

        }
    }

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination solrItempageUpdate;
    @Autowired
    private Destination solrItempageDelete;

    @Override
    public void updateIsMarketable(Long[] ids, String market) {

        //上架下架
        Map<String, Object> map = new HashMap<>();
        map.put("market", market);
        for (Long id : ids) {
            map.put("id", id);
            goodsMapper.updateIsMarketable(map);
        }
      /*  TbGoodsExample example = new TbGoodsExample();
        example.createCriteria().andIdIn(Arrays.asList(ids));
        TbGoods tbGoods = new TbGoods();
        tbGoods.setIsMarketable(market);
        goodsMapper.updateByExample(tbGoods,example);*/
		/*for (Long id : ids) {
			TbGoods tbGoods=goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsMarketable(market);
			goodsMapper.updateByPrimaryKey(tbGoods);

			update goods set isMa = ?
		}*/
//上架
        if (market.equals("1")) {
            for (Long id : ids) {
                jmsTemplate.send(solrItempageUpdate, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(id.toString());
                    }
                });
            }
        }
        //下架
        if (market.equals("2")) {
            for (Long id : ids) {
                jmsTemplate.send(solrItempageDelete, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        return session.createTextMessage(id.toString());
                    }
                });
            }
        }
    }
}

