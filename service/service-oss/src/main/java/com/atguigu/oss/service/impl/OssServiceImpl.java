package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.commonutils.ResultCodeEnum;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    @Override
    public String uploadFileAvatar(MultipartFile file) {
//      工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
//            获取文件名称
            String filename = file.getOriginalFilename();
//            重命名文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            filename = uuid + filename;
//            根据日期建立文件夹
            String datePath = new DateTime().toString("yyyy/MM/dd");
            filename = datePath + "/" + filename;
//            创建oss实例
            OSS ossClient = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
//            获取文件的上传的输入流
            InputStream inputStream = file.getInputStream();
//            调用方法实现上传
            ossClient.putObject(bucketName,filename,inputStream);
//            关闭流
            ossClient.shutdown();

            String url = "https://" + bucketName + "." + endpoint + "/" + filename;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.ERROR);
        }
    }
}
