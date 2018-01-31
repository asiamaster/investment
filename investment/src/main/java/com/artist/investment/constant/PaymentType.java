package com.artist.investment.constant;

import com.dili.ss.datasource.SwitchMode;

/**
 * 收支类型
 * Created by asiamaster on 2018/1/30
 */
public enum PaymentType {
	INCOME(1, "收入"),
	EXPENDITURE(2, "支出"),
	RECEIVABLE(3, "应收"),
	PAYABLE(4, "应付");
	private int code;
	private String text;

	PaymentType(int code, String text){
		this.code = code;
		this.text = text;
	}

	/**
	 * 根据类型编码获取SwitchMode
	 * @param code
	 * @return
	 */
	public static PaymentType getPaymentTypeByCode(int code){
		for(PaymentType paymentType : PaymentType.values()){
			if(paymentType.getCode() == code){
				return paymentType;
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
