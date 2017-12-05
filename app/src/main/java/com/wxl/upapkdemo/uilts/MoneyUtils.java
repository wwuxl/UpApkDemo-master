package com.wxl.upapkdemo.uilts;

import java.text.DecimalFormat;

/**
 * 作者: 何汉杰
 * 日期: 2016/12/15 时间: 13:28
 * 类:
 */
public class MoneyUtils {

    public static String getMoney(double value) {
        if (value <= 0) {
            value=0;
            return new DecimalFormat("0.00").format(value);
        }
        return new DecimalFormat("0.00").format(value);
    }

    /**
     *  一位小数
     * @param value
     * @return
     */
    public static String getMoney2(double value) {
        if (value <= 0) {
            value=0;
            return new DecimalFormat("0.0").format(value);
        }
        return new DecimalFormat("0.0").format(value);
    }
    /**
     *  没有小数
     * @param value
     * @return
     */
    public static String getMoney3(double value) {
        if (value <= 0) {
            value=0;
            return new DecimalFormat("0").format(value);
        }
        return new DecimalFormat("0").format(value);
    }

    /**
     * 判断金额是否合法
     */
    public static String judgeMoney(String money) {
        if (money.indexOf(".") == -1) {
            //整数直接调用递归
            return ridZero(money);
        } else {
            int index = money.indexOf(".");
            //第一位输入(.)点的话默认加个零
            if (index == 0) {
                return "0" + money;
            }
            //小数点之前加小数点之后;
            return ridZero(money.substring(0, index)) + money.substring(index, money.length());
        }
    }

    /**
     * 递归去零
     */
    public static String ridZero(String money) {
        //第一位等于0并且长度不等于1
        if (money.length() > 0) {
            if (money.substring(0, 1).equals("0") && money.length() != 1) {
                return ridZero(money.substring(1, money.length()));
            } else {
                return money;
            }
        }else{
            return "0";
        }
    }
}