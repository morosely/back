package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //把文件的内容存储到本地磁盘上
        String originalFilename = file.getOriginalFilename();
        //保证文件的名字是唯一的,从而防止文件覆盖
        String filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        //file.transferTo(new File("C:\\Users\\Administrator\\Desktop\\files\\"+filename));
        String url = AliOssUtil.uploadFile(filename,file.getInputStream());
        return Result.success(url);
    }


//    @Value("${file.upload-folder}")
//    private String UPLOAD_FOLDER;
//    @PostMapping("/upload")
//    public Result<String> upload(MultipartFile file) throws Exception {
//        //判断文件是否为空
//        if (file.isEmpty()) {
//            return Result.error("文件不能为空");
//        }
//        //判断文件大小
//        if (file.getSize() > 1024 * 1024 * 10) {
//            return Result.error("文件大小不能大于10M");
//        }
//        //获取文件后缀
//        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
//        if (!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())) {
//            Result.error("请选择jpg,jpeg,gif,png格式的图片");
//        }
//
//        try {
//            //上传的文件目录
//            File savePathFile = new File(UPLOAD_FOLDER);
//            if (!savePathFile.exists()) {
//                //若不存在该目录，则创建目录
//                savePathFile.mkdir();
//            }
//            // 重命名：解决中文问题，liunx下中文路径，图片显示问题
//            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
//            File destFile = new File(UPLOAD_FOLDER + fileName);
//            file.transferTo(destFile);
//            //返回相对路径
//            return Result.success( "/images/" + fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return Result.error("上传失败");
//    }
}
