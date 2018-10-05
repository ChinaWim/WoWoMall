package com.ming.wowomall.controller.backend;

import com.google.common.collect.Maps;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.Product;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.FileService;
import com.ming.wowomall.service.ProductService;
import com.ming.wowomall.service.UserService;
import com.ming.wowomall.util.CookieUtil;
import com.ming.wowomall.util.JsonUtil;
import com.ming.wowomall.util.PropertiesUtil;
import com.ming.wowomall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private FileService fileService;

    @RequestMapping("/list.do")
    public ServerResponse getProductList(HttpServletRequest request, @RequestParam(name = "pageNum",defaultValue = "1")Integer pageNum,
                                         @RequestParam(name = "pageSize",defaultValue = "10")Integer pageSize){
        return productService.getManageProductList(pageNum,pageSize);
    }


    @RequestMapping("/search.do")
    public ServerResponse searchProduct(HttpServletRequest request,String productName,Integer productId,@RequestParam(defaultValue = "1")Integer pageNum,@RequestParam(defaultValue = "10")Integer pageSize){

        return productService.getManageProductByNameOrProductId(productName,productId,pageNum,pageSize);
    }

    /**
     * 上传文件
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload.do",method = RequestMethod.POST)
    public ServerResponse upload(HttpServletRequest request,@RequestParam(name = "upload_file")MultipartFile file){

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


    @RequestMapping("/detail.do")
    public ServerResponse getProductDetail(HttpServletRequest request,Integer productId){

        return productService.getManageProductDetail(productId);
    }

    @RequestMapping(value = "/set_sale_status.do",method = RequestMethod.POST)
    public ServerResponse setSaleStatus(HttpServletRequest request,Integer productId,Integer status){
        return productService.updateProductStatus(productId,status);
    }

    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    public ServerResponse save(HttpServletRequest request,Product product){
        return productService.insertOrUpdate(product);
    }

    /**
     * 上传富文本
     * @param response
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(value = "/richtext_img_upload.do",method = RequestMethod.POST)
    public Map richtextImgUpload(HttpServletResponse response, HttpServletRequest request, @RequestParam(name = "upload_file")MultipartFile file){

        String classPath = ProductManageController.class.getResource("/").getPath();
        String fileName = fileService.uploadFile(file, classPath);
        //注意使用simditor,所以必须按照simditor的格式返回
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
            return resultMap;
        }

    }




}
