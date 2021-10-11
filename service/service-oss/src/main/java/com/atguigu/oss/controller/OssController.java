package com.atguigu.oss.controller;

import com.atguigu.commonutils.Result;
import com.atguigu.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description="阿里云文件管理")
@RestController
@RequestMapping("/eduoss/fileoss")
public class OssController {

    @Autowired
    private OssService ossService;


    /**
     * 头像上传
     * @param file
     * @return
     */
    @ApiOperation(value = "文件上传")
    @PostMapping
    public Result uploadFileOss(MultipartFile file){
        String avatar = ossService.uploadFileAvatar(file);
        return Result.ok(avatar);
    }
}
