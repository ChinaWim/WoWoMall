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

        private Integer code;

        private String value;

        ProductStatusEnum(Integer code,String value){
            this.code = code;
            this.value = value;
        }
        public Integer getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

}