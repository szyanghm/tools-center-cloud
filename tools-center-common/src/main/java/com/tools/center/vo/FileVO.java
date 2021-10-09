package com.tools.center.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileVO {

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件流
     */
    private InputStream inputStream;
}
