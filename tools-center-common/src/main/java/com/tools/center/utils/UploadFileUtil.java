package com.tools.center.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@Slf4j
public class UploadFileUtil {

    public static void uploadFile(){

    }


    /**
     * 文件上传
     * @param response
     * @param bytes 文件流
     * @param fileName 文件名
     */
    public static void downloadFile(HttpServletResponse response,byte[] bytes,String fileName){
        try {
            OutputStream outputStream = response.getOutputStream();
           response.setCharacterEncoding("UTF-8");
            response.setContentType("image/jpeg;charset=UTF-8");
            fileName = URLEncoder.encode(fileName,"utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            log.error("文件下载异常:{}",e.getMessage());
        }
    }
}
