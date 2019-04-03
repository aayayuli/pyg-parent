package com.pyg.itempage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.itempage.service.ItempageService;
import com.pyg.pojo.TbItem;
import freemarker.template.Configuration;
import freemarker.template.Template;
import groupEntity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/25  21:19
 * @description TODO
 **/
@RestController
@RequestMapping("itempage")
public class ItempageController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Reference
    private ItempageService itempageService;

    @RequestMapping("gen_item")
    public String gen_item(Long goodsId) throws Exception {

        Goods goods = itempageService.findOne(goodsId);

        //生成静态页面

//        第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就是freemarker 的版本号。
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
//        第二步：设置模板文件所在的路径。
//        第三步：设置模板文件使用的字符集。一般就是 utf-8.
        //2 3 步骤已在springmvc中配置
//        第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("item.ftl");

//        第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map。一般是 Map。

//将组合类goods 当成数据集 用来对应前端sku页面上的数据
        List<TbItem> itemList = goods.getItemList();
        for (TbItem tbItem : itemList) {
            //        第六步：创建一个 Writer 对象，一般创建一 FileWriter 对象，指定生成的文件名。
            Writer writer = new FileWriter("D:\\Test\\class61freemarker\\" + tbItem.getId() + ".html");
            Map map = new HashMap();
            map.put("goods", goods);
            map.put("tbItem", tbItem);
//        第七步：调用模板对象的 process 方法输出文件。
            template.process(map, writer);
//        第八步：关闭流
            writer.close();
        }

        return "success";
    }

    @RequestMapping("gen_itemAll")
    public String gen_itemAll(Long goodsId) throws Exception {

        List<Goods> goodsList = itempageService.findAll(goodsId);

        //生成静态页面

//        第一步：创建一个 Configuration 对象，直接 new 一个对象。构造方法的参数就是freemarker 的版本号。
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
//        第二步：设置模板文件所在的路径。
//        第三步：设置模板文件使用的字符集。一般就是 utf-8.
        //2 3 步骤已在springmvc中配置
//        第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate("item.ftl");

//        第五步：创建一个模板使用的数据集，可以是 pojo 也可以是 map。一般是 Map。
        for (Goods goods : goodsList) {
//将组合类goods 当成数据集 用来对应前端sku页面上的数据
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                //        第六步：创建一个 Writer 对象，一般创建一 FileWriter 对象，指定生成的文件名。
                Writer writer = new FileWriter("D:\\Test\\class61freemarker\\" + tbItem.getId() + ".html");
                Map map = new HashMap();
                map.put("goods", goods);
                map.put("tbItem", tbItem);
//        第七步：调用模板对象的 process 方法输出文件。
                template.process(map, writer);
//        第八步：关闭流
                writer.close();
            }
        }

        return "success";
    }


}
