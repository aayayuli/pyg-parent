package com.pyg.sellergoods.service;

import bean.PageResult;
import com.pyg.pojo.TbSpecification;
import groupEntity.Specification;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/8  10:11
 * @description TODO
 **/
public interface SpecificationService {
    public List<TbSpecification>  findAll();

    PageResult findPage(Integer pageNo, Integer pageSize);

    void add(Specification specification);

    Specification findOne(Long id);

    void update(Specification specification);

    void deleteByIds(Long[] ids);

    PageResult findPage(Integer pageNo, Integer pageSize, TbSpecification tbSpecification);

    List<Map> findSpecList();
}
