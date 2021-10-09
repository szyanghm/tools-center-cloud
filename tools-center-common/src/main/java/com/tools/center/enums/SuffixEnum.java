package com.tools.center.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum SuffixEnum {

    JPG(".jpg","image/jpeg"),
    PNG(".png","image/png"),
    ZIP(".zip","application/zip"),
    RAR(".rar","application/x-rar"),
    PDF(".pdf","application/pdf"),
    OFD(".ofd","application/ofd");

    private String suffix;
    private String centType;

    SuffixEnum(String suffix,String centType){
        this.suffix = suffix;
        this.centType = centType;
    }

    public void setSuffix(String suffix){
        this.suffix = suffix;
    }

    public void setContType(String contType){
        this.centType=contType;
    }

}
