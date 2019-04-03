package com.pyg.sellergoods.service.impl;

import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbSpecificationMapper;
import com.pyg.mapper.TbSpecificationOptionMapper;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;
import com.pyg.sellergoods.service.SpecificationService;
import groupEntity.Specification;
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
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private TbSpecificationMapper specificationMapper;
    @Autowired
    private TbSpecificationOptionMapper specificationOptionMapper;


    @Override
    public List<TbSpecification> findAll() {
        return specificationMapper.finAll();
    }

    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.finAll();
        return new PageResult(page.getTotal(), page.getResult());
        // page.getResult();//当前页的数据
        //page.getTotal(); //总条数
    }

    @Override
    public void add(Specification specification) {
        TbSpecification tbSpecification = specification.getTbSpecification();
        specificationMapper.insert(tbSpecification);
        System.out.println(specification);

        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }
    }

    @Override
    public Specification findOne(Long id) {
        Specification specification = new Specification();
        TbSpecification tbSpecification = specificationMapper.selectByKey(id);
        specification.setTbSpecification(tbSpecification);


        List<TbSpecificationOption> tbSpecificationOptionList = specificationOptionMapper.selectBySpecId(id);
        specification.setTbSpecificationOptionList(tbSpecificationOptionList);
        return specification;

    }

    @Override
    public void update(Specification specification) {
        TbSpecification tbSpecification = specification.getTbSpecification();
        specificationMapper.updateByKey(tbSpecification);
        //规格小项,先全删 后全insert
        specificationOptionMapper.deleteBySpecId(tbSpecification.getId());
        List<TbSpecificationOption> tbSpecificationOptionList = specification.getTbSpecificationOptionList();
        for (TbSpecificationOption tbSpecificationOption : tbSpecificationOptionList) {
            tbSpecificationOption.setSpecId(tbSpecification.getId());
            specificationOptionMapper.insert(tbSpecificationOption);
        }


    }

    @Override
    public void deleteByIds(Long[] ids) {
        for (Long id : ids) {
            specificationMapper.deleteByIds(id);
            //把规格小项中的数据一起删除
            specificationOptionMapper.deleteBySpecId(id);
        }
    }


    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize, TbSpecification tbSpecification) {
        PageHelper.startPage(pageNo, pageSize);
        Page<TbSpecification> page = (Page<TbSpecification>) specificationMapper.search(tbSpecification);
        return new PageResult(page.getTotal(), page.getResult());
    }


    @Override
    public List<Map> findSpecList() {
        return specificationMapper.findSpecList();
    }
}
