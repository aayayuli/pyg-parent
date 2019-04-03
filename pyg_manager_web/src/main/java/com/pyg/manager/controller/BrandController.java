package com.pyg.manager.controller;

import bean.Result;
import bean.PageResult;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbBrand;
import com.pyg.sellergoods.service.BrandService;
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
@RequestMapping("/brand")
public class BrandController {
    @Reference  //  远程注入  不同项目下
    private BrandService brandService;


    @RequestMapping("/findBrandList")
    public List<Map> findBrandList() {
        return brandService.findBrandList();
    }


    @RequestMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    @RequestMapping("findPage")
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        // {total:100,rows:[{},{}]}
        return brandService.findPage(pageNo, pageSize);
    }

    @RequestMapping("add")
    public Result add(@RequestBody TbBrand tbBrand) {
        // response ={success:true|false,message:"添加成功"|"添加失败"}
        try {
            brandService.add(tbBrand);
            return new Result(true, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }


    @RequestMapping("findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);

    }

    @RequestMapping("update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }

    }


    @RequestMapping("dele")
    public  Result dale( Long[] ids){
        try {
            brandService.deleteByIds(ids);
            return  new Result( true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"删除失败");
        }
    }

    @RequestMapping("search")
    public  PageResult search(Integer pageNo,Integer pageSize,@RequestBody TbBrand tbBrand){
       // {total:100,rows:[{},{}] }
        return brandService.findPage( pageNo,pageSize,tbBrand);
    }
}
