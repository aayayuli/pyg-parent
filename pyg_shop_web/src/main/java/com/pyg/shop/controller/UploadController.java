package com.pyg.shop.controller;

import bean.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

/**
 * @author dibulidubulijiojio
 * @version v1.0
 * @date 2019/3/17  22:25
 * @description TODO
 **/
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${upload_server}")//springmvc从properties中获取变量的方式
    private String uploadServer;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        //文件上传成功后 返回文件的url地址
        try {
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:fastdfs_client.conf");
            String filename = file.getOriginalFilename();
            String extName= filename.substring(filename.lastIndexOf(".")+1);
            String fileUrl = fastDFSClient.uploadFile(file.getBytes(), extName);
            return  new Result(true,uploadServer+fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,"上传失败");
        }
    }
}
