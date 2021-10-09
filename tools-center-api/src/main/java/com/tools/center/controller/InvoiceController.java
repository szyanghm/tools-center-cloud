package com.tools.center.controller;


import com.tools.center.enums.SuffixEnum;
import com.tools.center.utils.FileUtil;
import com.tools.center.utils.UploadFileUtil;
import com.tools.center.utils.ofd.OFDInfo;
import com.tools.center.utils.ofd.OfdParser;
import com.tools.center.utils.ofd.model.Invoice;
import com.tools.center.utils.ofd.utils.OfdUtils;
import com.tools.center.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Slf4j
@RestController
@RequestMapping(value = "/invoice")
public class InvoiceController {

    @PostMapping(value = "/getOfdToImage")
    public void getOfdToImage(@RequestParam("file") MultipartFile multipartFile, HttpServletResponse response){
        try {
            File file = FileUtil.multipartFileToFile(multipartFile);
            byte[] bytes = OfdUtils.ofdToImage(file);
            String fileName = FileUtil.getFileName(file.getName());
            UploadFileUtil.downloadFile(response,bytes,fileName+ SuffixEnum.JPG.getSuffix());
            FileUtil.delTempFile(file);
        } catch (Exception e) {
            log.error("文件转换失败异常:{}",e.getMessage());
        }
    }

    /**
     * 单张OFD发票解析
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "/getOfdInvoice",produces = {"application/json;charset=UTF-8"})
    public ResultVO getOfdInvoice(@RequestParam("file") MultipartFile multipartFile){
        try {
            File file = FileUtil.multipartFileToFile(multipartFile);
            OFDInfo ofdInfo = OfdParser.parse(file);
            Invoice invoice = ofdInfo.getInvoice();
            return ResultVO.success(invoice);
        } catch (Exception e) {
            log.error("文件解析失败异常:{}",e.getMessage());
        }
        return ResultVO.fail();
    }

    /**
     * 批量发票解析
     * @param multipartFile zip压缩包
     * @return
     */
    @PostMapping(value = "/getAllInvoice",produces = {"application/json;charset=UTF-8"})
    public ResultVO getAllInvoice(@RequestParam("file") MultipartFile multipartFile){
        try {
            File file = FileUtil.multipartFileToFile(multipartFile);
            return FileUtil.getAllInvoice(file,multipartFile.getSize(),2048L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVO.fail();
    }

    public static void main(String[] args) {
        File file = new File("D:\\java\\data\\ofd\\test.zip");
       // System.out.println(JSONObject.toJSONString(FileUtil.getAllInvoice(file,file.length(),2048L)));



    }

}
