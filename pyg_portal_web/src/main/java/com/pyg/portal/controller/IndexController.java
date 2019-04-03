package com.pyg.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.content.service.ContentService;
import com.pyg.pojo.TbContent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/22  14:55
 * @description TODO
 **/
@RestController
@RequestMapping("/index")
public class IndexController {
    @Reference
    private ContentService contentService;


@RequestMapping("findByCategoryId/{categoryId}")
    public List<TbContent> findByCategoryId(@PathVariable("categoryId") Long categoryId){

    List<TbContent> byCategoryId = contentService.findByCategoryId(categoryId);
    return byCategoryId;

    }
}
