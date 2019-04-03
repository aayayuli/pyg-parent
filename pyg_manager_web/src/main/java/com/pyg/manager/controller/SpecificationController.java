package com.pyg.manager.controller;

import bean.Result;
import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbSpecification;
import com.pyg.sellergoods.service.SpecificationService;
import groupEntity.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/8  10:31
 * @description TODO
 **/
@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Reference  //  远程注入  不同项目下
    private SpecificationService specificationService;
    @RequestMapping("/findSpecList")
    public List<Map> findSpecList() {
        return specificationService.findSpecList();
    }

    @RequestMapping("/findAll")
    public List<TbSpecification> findAll() {
        return specificationService.findAll();
    }

    @RequestMapping("findPage")
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        // {total:100,rows:[{},{}]}
        return specificationService.findPage(pageNo, pageSize);
    }

    @RequestMapping("add")
    public Result add(@RequestBody Specification specification) {
        // response ={success:true|false,message:"添加成功"|"添加失败"}
        try {
            specificationService.add(specification);
            return new Result(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }


    @RequestMapping("findOne")
    public Specification findOne(Long id) {
        return specificationService.findOne(id);

    }

    @RequestMapping("update")
    public Result update(@RequestBody Specification specification) {
        try {
            specificationService.update(specification);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }


    @RequestMapping("dele")
    public  Result dale( Long[] ids){
        try {
            specificationService.deleteByIds(ids);
            return  new Result( true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"删除失败");
        }
    }

    @RequestMapping("search")
    public  PageResult search(Integer pageNo,Integer pageSize,@RequestBody TbSpecification tbSpecification){
       // {total:100,rows:[{},{}] }
        return specificationService.findPage( pageNo,pageSize,tbSpecification);
    }
}
