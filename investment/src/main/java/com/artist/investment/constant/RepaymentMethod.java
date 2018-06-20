package com.artist.investment.constant;

/**
 * 还款方式
 * Created by asiam on 2018/6/15 0015.
 */
public enum RepaymentMethod {
    DUE(1, "到期还款"),
    MONTHLY(2, "每月付息"),
    PRINCIPAL_INTEREST(3, "等额本息");
    private int code;
    private String text;

    RepaymentMethod(int code, String text){
        this.code = code;
        this.text = text;
    }

    /**
     * 根据类型编码获取SwitchMode
     * @param code
     * @return
     */
    public static RepaymentMethod getRepaymentMethodByCode(int code){
        for(RepaymentMethod repaymentMethod : RepaymentMethod.values()){
            if(repaymentMethod.getCode() == code){
                return repaymentMethod;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
