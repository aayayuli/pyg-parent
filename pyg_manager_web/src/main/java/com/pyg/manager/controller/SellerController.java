package com.pyg.manager.controller;

import bean.PageResult;
import bean.Result;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pojo.TbSeller;
import com.pyg.sellergoods.service.SellerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

	@Reference
	private SellerService sellerService;

	/**
	 * 审核状态
	 */
	@RequestMapping("updateStatus/{status}/{sellerId}")
	public Result updateStatus(@PathVariable("status") String status,@PathVariable("sellerId") String sellerId){
		try {
			sellerService.updateStatus(status,sellerId);
			return  new Result(true,null);
		} catch (Exception e) {
			e.printStackTrace();
			return  new Result(false,"状态修改失败");
		}

	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSeller> findAll(){			
		return sellerService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int pageNum,int pageSize){			
		return sellerService.findPage(pageNum, pageSize);
	}
	
	/**
	 * 增加
	 * @param seller
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSeller seller){
		try {
			sellerService.add(seller);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param seller
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSeller seller){
		try {
			sellerService.update(seller);
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
	public TbSeller findOne(String id){
		return sellerService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/dele")
	public Result delete(String [] ids){
		try {
			sellerService.dele(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param seller
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbSeller seller, int pageNum, int pageSize  ){
		return sellerService.findPage(seller, pageNum, pageSize);		
	}
	
}
