package com.pyg.sellergoods.service.impl;

import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbBrandMapper;
import com.pyg.pojo.TbBrand;
import com.pyg.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/8  10:14
 * @description TODO
 **/
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.finAll();
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.finAll();
        return new PageResult(page.getTotal(), page.getResult());
        // page.getResult();//当前页的数据
        //page.getTotal(); //总条数
    }

    @Override
    public void add(TbBrand tbBrand) {
        brandMapper.insert(tbBrand);

    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.selectByKey(id);

    }

    @Override
    public void update(TbBrand tbBrand) {
        brandMapper.updateByKey(tbBrand);
    }

    @Override
    public void deleteByIds(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByIds(id);
        }
    }


    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize, TbBrand tbBrand) {
        PageHelper.startPage(pageNo,pageSize);
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.search(tbBrand);
        return new PageResult(page.getTotal(),page.getResult());
    }


    @Override
    public List<Map> findBrandList() {
        return brandMapper.findBrandList();
    }
}
