package com.ming.wowomall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ResponseCode;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.pojo.User;
import com.ming.wowomall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-1 下午4:07
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);


    /**
     * 创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("/create.do")
    public Object create(HttpSession session,Integer shippingId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.insertOrder(currentUser.getId(),shippingId);
    }

    /**
     * 获取订单的商品信息
     * @param session
     * @return
     */
    @RequestMapping("/get_order_cart_product.do")
    public Object getOrderCartProduct(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderCartProduct(currentUser.getId());
    }

    /**
     * 支付
     * @param orderNo
     * @param session
     * @param request
     * @return
     */
    @RequestMapping("/pay.do")
    public Object pay(Long orderNo,HttpSession session, HttpServletRequest request){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        //二维码本地保存路径
        String path = request.getServletContext().getRealPath("/upload");
        return orderService.pay(currentUser.getId(),orderNo,path);
    }

    /**
     * 查询订单状态
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("/query_order_pay_status.do")
    public Object queryOrderPayStatus(HttpSession session,Long orderNo){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderPayStatus(currentUser.getId(),orderNo);
    }

    /**
     * 扫码支付回调
     * @param request
     * @return
     */
    @RequestMapping("/alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        Map<String,String> params = new HashMap<>();
        for (Map.Entry<String,String[]> entry : requestParameterMap.entrySet()) {
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr += i == values.length - 1 ? values[i] : values[i] + ",";
            }
            params.put(entry.getKey(),valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
        try {
            params.remove("sign_type");
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params,Configs.getPublicKey(),"utf-8",Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求!再恶意请求我就报警找网管了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验签错误",e);
        }

        //验证通过回调数据
        ServerResponse serverResponse = orderService.aliCallback(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    @RequestMapping("/list.do")
    public Object list(HttpSession session, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "1")Integer pageNum){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.listOrderByUserId(currentUser.getId(),pageNum,pageSize);
    }

    @RequestMapping("/detail.do")
    public Object detail(HttpSession session,Long orderNo){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        return orderService.getOrderDetail(currentUser.getId(),orderNo);
    }


    @RequestMapping("/cancel.do")
    public Object cancel(HttpSession session,Long orderNo){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.cancelOrder(currentUser.getId(),orderNo);
    }







}
