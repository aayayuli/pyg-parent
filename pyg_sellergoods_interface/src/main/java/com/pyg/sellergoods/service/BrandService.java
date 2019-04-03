package com.pyg.sellergoods.service;

import bean.Result;
import bean.PageResult;
import com.pyg.pojo.TbBrand;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/8  10:11
 * @description TODO
 **/
public interface BrandService {
    public List<TbBrand>  findAll();

    PageResult findPage(Integer pageNo, Integer pageSize);

    void add(TbBrand tbBrand);

    TbBrand findOne(Long id);

    void update(TbBrand tbBrand);

    void deleteByIds(Long[] ids);

    PageResult findPage(Integer pageNo, Integer pageSize, TbBrand tbBrand);

    List<Map> findBrandList();
}
