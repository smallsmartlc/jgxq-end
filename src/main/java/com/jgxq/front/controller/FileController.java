package com.jgxq.front.controller;

import com.jgxq.core.enums.CommonErrorCode;
import com.jgxq.core.exception.SmartException;
import com.jgxq.core.resp.ResponseMessage;
import com.jgxq.front.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-09
 **/
@RestController
@RequestMapping("file")
public class FileController {

    /**
     * 默认大小 5M
     */
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    @Autowired
    private FileServiceImpl fileService;

    @PostMapping("img/{project}/{folder}")
    public ResponseMessage uploadImg(@RequestParam("file") MultipartFile file,
                                     @PathVariable("project") String project,
                                     @PathVariable("folder") String folder) {
        if (file.isEmpty()) {
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"文件为空");
        }
        //判断文件是否为空文件
        if (file.getSize() <= 0) {
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"文件为空");
        }
        // 判断文件大小不能大于5M
        if (DEFAULT_MAX_SIZE != -1 && file.getSize() > DEFAULT_MAX_SIZE) {
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"上传的文件不能大于10M");
        }
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        // 检查是否是图片
        List<String> allowSuffix = new ArrayList<>(Arrays.asList(".jpg", ".jpeg", ".png", ".gif"));
        if (!allowSuffix.contains(suffix.toLowerCase())) {
            throw new SmartException(CommonErrorCode.BAD_PARAMETERS.getErrorCode(),"不能识别的图片格式");
        }

        String targetpath = "images/" + project + "/" + folder;

        String res = fileService.uploadImg(targetpath,file);

        if(res == null){
            return new ResponseMessage("400", "文件上传失败", null);
        }
        return new ResponseMessage(res);
    }

}
