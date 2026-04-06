package com.changgou.files.controller;
import com.changgou.files.util.FastDFSClient;
import com.changgou.files.util.FastDFSFile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.changgou.common.Result;
import com.changgou.common.StatusCode;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FilesController {

    @PostMapping("/downFile")
    public Result downFile(String groupName,String remoteFileName) throws Exception{
        if(StringUtils.isEmpty(groupName) && StringUtils.isEmpty(remoteFileName)) {
            return new Result(false, StatusCode.ERROR, "参数不能为空", null);
        }
        InputStream fis  = FastDFSClient.downFile(groupName,remoteFileName);
        String downPath = "C:\\Users\\YiHaiTao\\Desktop\\downFile.jpg";
        FileOutputStream ios = new FileOutputStream(downPath);
        //定义一个字节数组
        byte[] buf = new byte[1024];
        while (fis.read(buf)!=-1){
            ios.write(buf);
        }
        ios.flush();;
        ios.close();
        fis.close();
        return new Result(true,StatusCode.OK,"下载成功","");
    }

    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file){
        try{
            //判断文件是否存在
            if (file == null){
                throw new RuntimeException("文件不存在");
            }
            //获取文件的完整名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)){
                throw new RuntimeException("文件不存在");
            }

            //获取文件的扩展名称  abc.jpg   jpg
            //String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String extName = StringUtils.getFilenameExtension(originalFilename);

            //获取文件内容
            byte[] content = file.getBytes();

            //创建文件上传的封装实体类
            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename,content,extName);

            //基于工具类进行文件上传,并接受返回参数  String[]
            String[] uploadResult = FastDFSClient.upload(fastDFSFile);

            //封装返回结果
            String url = FastDFSClient.getTrackerUrl()+uploadResult[0]+"/"+uploadResult[1];
            return new Result(true,StatusCode.OK,"文件上传成功",url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR,"文件上传失败");
    }
}