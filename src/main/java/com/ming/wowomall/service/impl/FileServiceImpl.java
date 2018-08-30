package com.ming.wowomall.service.impl;

import com.google.common.collect.Lists;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.service.FileService;
import com.ming.wowomall.util.FTPUtil;
import com.ming.wowomall.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-8-30 下午4:01
 */
@Service
public class FileServiceImpl implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile uploadFile, String dirPath) {
        String filename = uploadFile.getOriginalFilename();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        String newFileName = UUIDUtil.createByTime() + "." + fileType;
        File fileDir = new File(dirPath);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        try {
            logger.info("上传文件中,上传文件名:{},新文件名:{},上传路径:{}"+filename,newFileName,dirPath);
            File targetFile = new File(fileDir,newFileName);
            uploadFile.transferTo(targetFile);
            boolean result = FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
            if (!result) {
                return null;
            }
        } catch (IOException e) {
            logger.info("上传文件异常:"+e);
            return null;
        }
        return newFileName;
    }
}
