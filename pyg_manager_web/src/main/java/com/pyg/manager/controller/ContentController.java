package com.pyg.manager.controller;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbContent;
import com.pyg.content.service.ContentService;

import bean.PageResult;
import bean.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbContent> findAll(){			
		return contentService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int pageNum,int pageSize){			
		return contentService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param content
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbContent content){
		try {
			contentService.add(content);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param content
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbContent content){
		try {
			contentService.update(content);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbContent findOne(Long id){
		return contentService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public Result delete(Long [] ids){
		try {
			contentService.dele(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param content
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbContent content, int pageNum, int pageSize  ){
		return contentService.findPage(content, pageNum, pageSize);		
	}
	
}
