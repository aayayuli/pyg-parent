package com.pyg.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.search.service.SearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/23  9:34
 * @description TODO
 **/
@RestController
@RequestMapping("/search")
public class SearchController {
   @Reference
    private SearchService searchService;

    @RequestMapping("/searchByParamMap")
    public Map searchByParamMap(@RequestBody Map paramMap) {
        return  searchService.searchByParamMap(paramMap);
    }
}
