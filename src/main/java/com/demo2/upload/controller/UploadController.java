package com.demo2.upload.controller;

import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.common.Config;
import com.demo2.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 上传
 * Created by demo2 on 2016/11/11.
 */
@RestController
@RequestMapping("/api")
public class UploadController {
    private Config config;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws AppException {
        String path = this.upload(config.getUploadFolder(), file);
        if(path.length() > 200) {
            throw new AppException(MessageCode.FILENAME_TOO_LONG);
        }
        return new UploadResult(true, "upload" + File.separator + path, config.getDomain());
    }

    private String upload(String folder, MultipartFile file) throws AppException {
        if(!folder.endsWith(File.separator)){
            folder = folder + "/";
        }

        if(!new File(folder).exists()){
            new File(folder).mkdirs();
        }

        String name = file.getOriginalFilename();
        // Make sure the fileName is unique
        if(new File(folder + name).exists()){
            int index = name.lastIndexOf(".");
            String fileName = name.substring(0, index);
            String ext = name.substring(index);
            int count = 1;
            File aFile = new File(folder + fileName + "_" + count + ext);
            while (aFile.exists()){
                count++;
                aFile = new File(folder + fileName + "_" + count + ext);
            }
            name = fileName + "_" + count + ext;
        }

        String filePath = folder + name;

        if(!file.isEmpty()){//如果不空
            try {
                Files.copy(file.getInputStream(), Paths.get(folder).resolve(name));
            } catch (IOException e) {
                throw new AppException(MessageCode.FILE_WRITE_ERROR, e.getMessage());
            }
        }
        return filePath.substring(folder.length());
    }

    @Resource
    public void setConfig(Config config) {
        this.config = config;
    }
}

class UploadResult extends Result{
    UploadResult(boolean success, String filePath, String imageDomain){
        super(success, filePath);
        this.file_path = imageDomain + filePath;
    }
    UploadResult(boolean success, Integer id, String filePath, String imageDomain){
        super(success, filePath);
        this.id = id;
        this.file_path = imageDomain + filePath;
    }
    private Integer id;
    private String file_path;

    public String getFile_path() {
        return file_path;
    }

    public Integer getId() {
        return id;
    }
}
