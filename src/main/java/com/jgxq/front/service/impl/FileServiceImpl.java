package com.jgxq.front.service.impl;

import com.jgxq.core.resp.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author LuCong
 * @since 2020-12-09
 **/
@Service
public class FileServiceImpl {


    @Value("${web.upload-path}")
    private String mImagesPath;


    public String uploadImg(String targetpath, MultipartFile file) {

        String pathname = mImagesPath + targetpath;

        File filePath = new File(pathname);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdirs();
        }

        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        String newName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
        File targetFile = new File(filePath, newName);

        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            return null;
        }

        return targetpath +'/'+ newName;
    }
}
