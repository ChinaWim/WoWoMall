package com.ming.wowomall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-8-30 下午4:03
 */

public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");

    private static String ftpPassword = PropertiesUtil.getProperty("ftp.password");

    private static String ftpIp = PropertiesUtil.getProperty("ftp.ip");


    private String ip;

    private Integer port;

    private String user;

    private String password;

    private FTPClient ftpClient;

    public FTPUtil(String ip,Integer port,String user,String password){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;

    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPassword);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img",fileList);
        logger.info("结束连接ftp服务器,结束上传,上传结果:{}",result);
        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if(connectServer(this.ip,this.port,this.user,this.password)){
            try {
                //todo
                //切换工作目录
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //打开被动模式
                ftpClient.enterLocalPassiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    boolean isStored = ftpClient.storeFile(file.getName(), fis);
                    System.out.println(isStored);
                }
            } catch (IOException e) {
                uploaded = false;
                logger.error("ftp上传文件异常", e);
            } finally {
                if(fis != null){
                    fis.close();
                }
                ftpClient.disconnect();
            }
            return uploaded;
        }else {
            return false;
        }
    }
    private boolean connectServer(String ip,int port,String user,String password){
        boolean isConnected = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip,port);
            isConnected = ftpClient.login(user, password);
        } catch (IOException e) {
            logger.error("ftp连接异常", e);
        }
        return isConnected;
    }










}
