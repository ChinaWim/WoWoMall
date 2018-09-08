package com.ming.wowomall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量类
 *
 * @author m969130721@163.com
 * @date 18-5-27 下午6:43
 */
public class Const {

    public static final String CURRENT_USER = "current_user";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface Cart{
        int CHECKED = 1;
        int UN_CHECKED = 0;

        String LIMI_NUM_FAIL = "LIMI_NUM_FAIL";

        String LIMI_NUM_SUCCESS = "LIMI_NUM_SUCCESS";
    }


    public enum ProductStatusEnum {
        /**
         *　商品上架状态
         */
        ON_SALE(1,"在售");

        private int code;

        private String value;

        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }




    public enum OrderStatusEnum {
        /**
         *订单已取消
         */
        CANCELED(0,"已取消"),
        /**
         *订单未支付
         */
        NO_PAY(10,"未支付"),
        /**
         *订单已付款
         */
        PAID(20,"已付款"),
        /**
         *订单已发货
         */
        SHIPPED(40,"已发货"),
        /**
         *订单完成
         */
        ORDER_SUCCESS(50,"订单完成"),
        /**
         *订单关闭
         */
        ORDER_CLOSE(60,"订单关闭");

        private int code;

        private String value;

        OrderStatusEnum(int code ,String value){
                this.code = code;
                this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static String codeOfValue(Integer code){
            OrderStatusEnum[] values = OrderStatusEnum.values();
            for (OrderStatusEnum orderStatusEnum : values) {
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum.getValue();
                }
            }
            return null;
        }

    }

    public interface AlipayCallback {
        /**
         * alipay交易创建，等待买家付款
         */
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

        /**
         *alipay交易支付成功
         */
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        /**
         * alipay未付款交易超时关闭，或支付完成后全额退款
         */
        String TRADE_STATUS_TRADE_CLOSED = "TRADE_CLOSED";

        /**
         * wowomall系统成功
         */
        String RESPONSE_SUCCESS = "success";

        /**
         *wowomall系统失败
         */
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum{
        /**
         * 支付宝平台
         */
        ALIPAY(1,"支付宝"),
        /**
         * 微信平台
         */
        WECHAT(2,"微信");

        private int code;

        private String value;

        PayPlatformEnum(int code ,String value){
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum{
        /**
         * 在线支付
         */
        ONLINE(1,"在线支付");

        private  int code;

        private String value;

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
        public static String codeOfValue(Integer code){
            PaymentTypeEnum[] values = PaymentTypeEnum.values();
            for (PaymentTypeEnum paymentTypeEnum : values) {
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum.getValue();
                }
            }
            return null;
        }

    }




}
