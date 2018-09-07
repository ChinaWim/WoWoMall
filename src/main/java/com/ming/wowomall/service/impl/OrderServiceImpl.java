package com.ming.wowomall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ming.wowomall.common.Const;
import com.ming.wowomall.common.ServerResponse;
import com.ming.wowomall.dao.OrderItemMapper;
import com.ming.wowomall.dao.OrderMapper;
import com.ming.wowomall.dao.PayInfoMapper;
import com.ming.wowomall.dao.ShippingMapper;
import com.ming.wowomall.pojo.Order;
import com.ming.wowomall.pojo.OrderItem;
import com.ming.wowomall.pojo.PayInfo;
import com.ming.wowomall.service.CartService;
import com.ming.wowomall.service.OrderService;
import com.ming.wowomall.service.PayInfoService;
import com.ming.wowomall.util.*;
import com.ming.wowomall.vo.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author m969130721@163.com
 * @date 18-9-1 下午4:08
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PayInfoMapper payInfoMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private ShippingMapper shippingMapper;


    /**
     * 支付宝当面付2.0服务
     */
    private static AlipayTradeService   tradeService;

    static{
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
         tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    private static  Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);



    @Override
    public ServerResponse<OrderVO> insertOrder(Integer userId, Integer shippingId) {
        Order order = new Order();
        order.setShippingId(shippingId);
        //order.setOrderNo();
        orderMapper.insertSelective(order);

        //project
        ServerResponse<CartVO> response = cartService.list(userId);
        CartVO carVO = response.getData();
        List<CartProductVO> cartProductVoList = carVO.getCartProductVoList();
        List<OrderItemVO> orderItemVoList = assembledOrderItemVoList(cartProductVoList);
        //封装数据
        OrderVO orderVO = new OrderVO();

        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setPayment(carVO.getCartTotalPrice());
        orderVO.setPaymentType(Const.PayPlatformEnum.ALIPAY.getCode());
        //运费
        orderVO.setPostage(0);
        orderVO.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        orderVO.setShippingId(shippingId);
        orderVO.setOrderItemVoList(orderItemVoList);

        return ServerResponse.createBySuccess(orderVO);
    }

    @Override
    public ServerResponse<Map> pay(Integer userId, Long orderNo, String path) {
        //返回值封装
        //"orderNo": "1485158676346",
        //"qrPath": "http://img.wowomall.com/qr-1492329044075.png"
        Map resultMap = Maps.newHashMap();
        Order order = orderMapper.getByUserIdOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }
        resultMap.put("orderNo",orderNo);


        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderNo.toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("wowomall扫码支付,订单号:").append(orderNo).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共:").append(totalAmount).append("元").toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        List<OrderItem> orderItems = orderItemMapper.listByUserIdOrderNo(userId, orderNo);
        for (OrderItem good : orderItems){
            GoodsDetail goods = GoodsDetail.newInstance(String.valueOf(good.getId()), good.getProductName(),
                        BigDecimalUtil.multiply(good.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                        good.getQuantity());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods);
        }

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                //生成二维码上传到ftp服务器上
                File file = new File(path);
                if (!file.exists()) {
                    file.setWritable(true);
                    file.mkdirs();
                }
                String qrFileName = String.format("qr-%s.png",response.getOutTradeNo());
                String qrPath = path + "/" + qrFileName;
                logger.info("qrPath:" + qrPath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File targetFile = new File(path,qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常",e);
                }
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrPath",qrUrl);

                targetFile.delete();
                return  ServerResponse.createBySuccess(resultMap);
            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return  ServerResponse.createByErrorMessage("支付宝预下单失败!!!");
            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return  ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");
            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return  ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }



    @Override
    public ServerResponse<Boolean> getOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.getByUserIdOrderNo(userId, orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该用户并没有该订单,查询无效");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

    // 支付宝简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerResponse aliCallback(Map<String,String> params){
        /*商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        同时需要校验通知中的seller_id（或者seller_email)是否为out_trade_no这笔单据的对应的操作方
        （有的时候，一个商户可能有多个seller_id/seller_email），上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
        在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。在支付宝的业务通知中，
        只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。*/
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");


        Order order = orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("非wowomall的订单,回调忽略");
        }
        if (!params.get("total_amount").equals(order.getPayment().toString())) {
            return ServerResponse.createByErrorMessage("订单金额被篡改,回调忽略");
        }
        String[] sellerIds = params.get("seller_id").split(",");
        for (String sellerId : sellerIds) {
            if (!sellerId.equals(Configs.getPid())) {
                return ServerResponse.createByErrorMessage("订单卖家错误,回调忽略");
            }
        }

        //返回success
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess("支付宝重复调用");
        }

        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            //支付时间
            order.setPaymentTime(DateTimeUtil.strToDate(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode() );
            orderMapper.updateByPrimaryKey(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        int effectRow = payInfoMapper.insertSelective(payInfo);

        return effectRow == 0 ? ServerResponse.createByError() : ServerResponse.createBySuccess();

    }

    @Override
    public ServerResponse<Map> getOrderCartProduct(Integer userId) {

        ServerResponse<CartVO> response = cartService.list(userId);

        CartVO cartVO = response.getData();
        List<CartProductVO> cartProductVoList = cartVO.getCartProductVoList();
        List<OrderItemVO> orderItemVoList = assembledOrderItemVoList(cartProductVoList);
        Map resultMap = Maps.newHashMap();
        resultMap.put("orderItemVoList",orderItemVoList);
        resultMap.put("imageHost",cartVO.getImageHost());
        resultMap.put("productTotalPrice",cartVO.getCartTotalPrice());

        return ServerResponse.createBySuccess(resultMap);
    }

    @Override
    public ServerResponse<PageInfo> listOrderByUserId(Integer userId,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,true);
        List<OrderVO> orderVOList = orderMapper.listOrderVOByUserId(userId);
        for (OrderVO orderVO : orderVOList) {
            orderVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
            orderVO.setReceiverName("");
            List<OrderItemVO> orderItemVOList = orderItemMapper.listOrderItemVOByUserIdOrderNo(userId, orderVO.getOrderNo());
            orderVO.setOrderItemVoList(orderItemVOList);
        }
        PageInfo<OrderVO> pageInfo = new PageInfo(orderVOList);

        return ServerResponse.createBySuccess(pageInfo);
    }

    @Override
    public ServerResponse getOrderDetail(Integer userId, Long orderNo) {
        OrderVO orderVO = orderMapper.getOrderVOByOrderNo(orderNo);
        if (orderVO == null) {
            return ServerResponse.createByErrorMessage("没有找到订单");
        }
        orderVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        ShippingVO shippingVO = shippingMapper.getShippingVOById(orderVO.getShippingId());
        orderVO.setShippingVo(shippingVO);

        orderVO.setReceiverName(shippingVO.getReceiverName());

        List<OrderItemVO> orderItemVOList = orderItemMapper.listOrderItemVOByUserIdOrderNo(userId, orderNo);
        orderVO.setOrderItemVoList(orderItemVOList);

        return ServerResponse.createBySuccess(orderVO);
    }

    @Override
    public ServerResponse cancelOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            return ServerResponse.createByErrorMessage("该用户没有此订单");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createByErrorMessage("此订单已付款，无法被取消");
        }
        int effectRow = orderMapper.updateOrderStatus(userId,orderNo,Const.OrderStatusEnum.CANCELED.getCode());

        return effectRow > 0 ? ServerResponse.createBySuccess() : ServerResponse.createByError();
    }


    private  List<OrderItemVO>  assembledOrderItemVoList(List<CartProductVO> cartProductVoList){
        List<OrderItemVO> orderItemVoList = Lists.newArrayList();
        for (CartProductVO cartProductVO : cartProductVoList) {
            OrderItemVO orderItemVO = new OrderItemVO();
//            orderItemVO.setOrderNo(order.getOrderNo());
            orderItemVO.setProductId(cartProductVO.getProductId());
            orderItemVO.setProductName(cartProductVO.getProductName());
            orderItemVO.setProductImage(cartProductVO.getProductMainImage());
            orderItemVO.setCurrentUnitPrice(cartProductVO.getProductPrice());
            orderItemVO.setQuantity(cartProductVO.getQuantity());
            orderItemVO.setTotalPrice(cartProductVO.getProductTotalPrice());
            orderItemVO.setCreateTime(new Date());

            orderItemVoList.add(orderItemVO);
        }
        return orderItemVoList;
    }

}
