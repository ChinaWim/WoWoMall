package com.ming.wowomall.controller.backend;

import com.google.common.collect.Maps;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.FileService;
import com.ming.wowomall.service.ProductService;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-8-29 下午9:57
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @GetMapping("/list.do")
    public Object getProductList(HttpSession session, @RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return productService.getManageProductList(pageNum,pageSize);
    }


    @GetMapping("/search.do")
    public Object searchProduct(HttpSession session,String productName,Integer productId,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return productService.getProductByNameOrProductId(productName,productId,pageNum,pageSize);
    }

    /**
     * 上传文件
     * @param session
     * @param file
     * @return
     */
    @PostMapping("/upload.do")
    public Object upload(HttpSession session,@RequestParam(name = "upload_file")MultipartFile file){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        String classPath = ProductManageController.class.getResource("/").getPath();
        String fileName = fileService.uploadFile(file, classPath);
        if (StringUtils.isBlank(fileName)) {
            return ServerResponse.createByError();
        } else {
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("uri", fileName);
            resultMap.put("url", url);
            return ServerResponse.createBySuccess(resultMap);
        }
    }


    @GetMapping("/detail.do")
    public Object getProductDetail(HttpSession session,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }
        return productService.getProductDetail(productId);
    }

    @PostMapping("/set_sale_status.do")
    public Object setSaleStatus(HttpSession session,Integer productId,Integer status){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        return productService.updateProductStatus(productId,status);
    }

    @PostMapping("/save.do")
    public Object save(HttpSession session,Product product){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }

        return productService.insertOrUpdate(product);
    }

    /**
     * 上传富文本
     * @param response
     * @param session
     * @param file
     * @return
     */
    @PostMapping("richtext_img_upload.do")
    public Object richtextImgUpload(HttpServletResponse response,HttpSession session, @RequestParam(name = "upload_file")MultipartFile file){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if (!userService.checkRoleAdmin(currentUser).isSuccess()){
            return ServerResponse.createByErrorMessage("没有权限");
        }

        String classPath = ProductManageController.class.getResource("/").getPath();
        String fileName = fileService.uploadFile(file, classPath);
        Map<String,Object> resultMap = Maps.newHashMap();
        if (StringUtils.isBlank(fileName)) {
            resultMap.put("success",false);
            resultMap.put("msg","上传文件失败");
            resultMap.put("file_path",classPath + file.getOriginalFilename());
            return resultMap;
        } else {
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传文件成功");
            resultMap.put("file_path",url);
            //simditor富文本编辑器要求的响应头
            response.setHeader("Access-Control-Allow-Headers","X-File-Name");
            return ServerResponse.createBySuccess(resultMap);
        }

    }




}
