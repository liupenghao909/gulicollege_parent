package com.ant.serviceStatistics.enumeration;

public enum StatisticsSearchTypeEnum {
    REGISTER_NUM("register_num","registerNum"),
    LOGIN_NUM("login_num","loginNum"),
    VIDEO_VIEW_NUM("video_view_num","videoViewNum"),
    COURSE_NUM("course_num","courseNum");

    private final String column;

    private final String filed;

    StatisticsSearchTypeEnum(String column, String field){
        this.column = column;
        this.filed = field;
    }

    public String getColumn() {
        return column;
    }

    public String getFiled() {
        return filed;
    }
}
