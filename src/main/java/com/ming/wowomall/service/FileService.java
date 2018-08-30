package com.ming.wowomall.service;

import com.ming.wowomall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author m969130721@163.com
 * @date 18-8-30 下午4:01
 */
public interface FileService {

    /**
     *
     * @param file
     * @param dirPath
     * @return　上传后新的文件名
     */
    String uploadFile(MultipartFile file, String dirPath);

}
