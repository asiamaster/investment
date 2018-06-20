package com.artist.investment.constant;

/**
 * 项目期限单位
 * Created by asiam on 2018/6/20 0020.
 */
public enum ProjectDurationUnit {
    DAY(1, "天"),
    WEEK(2, "周"),
    MONTH(3, "月"),
    SEASON(4, "季"),
    YEAR(5, "年");

    private int code;
    private String text;

    ProjectDurationUnit(int code, String text){
        this.code = code;
        this.text = text;
    }

    /**
     * 根据类型编码获取SwitchMode
     * @param code
     * @return
     */
    public static ProjectDurationUnit getProjectDurationUnit(int code){
        for(ProjectDurationUnit projectDurationUnit : ProjectDurationUnit.values()){
            if(projectDurationUnit.getCode() == code){
                return projectDurationUnit;
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