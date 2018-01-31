package com.artist.investment.constant;

/**
 * 是否
 * Created by asiamaster on 2018/1/30
 */
public enum Yn {
	YES(1, "是"),
	NO(0, "否");

	private int code;
	private String text;

	Yn(int code, String text){
		this.code = code;
		this.text = text;
	}

	/**
	 * 根据类型编码获取SwitchMode
	 * @param code
	 * @return
	 */
	public static Yn getYnByCode(int code){
		for(Yn yn : Yn.values()){
			if(yn.getCode() == code){
				return yn;
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
