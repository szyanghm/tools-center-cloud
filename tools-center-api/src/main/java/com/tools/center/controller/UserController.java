package com.tools.center.controller;

import com.alibaba.fastjson.JSONObject;
import com.tools.center.TestService;
import com.tools.center.UserService;
import com.tools.center.utils.pdf.Invoice;
import com.tools.center.utils.pdf.InvoiceData;
import com.tools.center.utils.pdf.InvoiceExtractor;
import com.tools.center.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController{

    @Autowired
    private TestService testService;

    @PostMapping("/cityArr")
    public ResultVO arr(@RequestBody List<Test> list){
        System.out.println(JSONObject.toJSONString(list.get(0)));
        List<Test> bulid = TestTreeUtil.bulid(list, "0");
        System.out.println(JSONObject.toJSONString(bulid.get(0)));
        return ResultVO.success(bulid);
    }

//
//    @PostMapping(value = "/a01",produces = {"application/json;charset=UTF-8"})
//    public ResultVO test(@RequestBody Test test){
//        return testService.test(test);
//    }


    public static void main(String[] args) {
        String filename = "D:\\java\\data\\pdf\\INVOICE.pdf";
        try {
            InvoiceData data = InvoiceExtractor.extract(filename);

            Invoice invoice = data.getInvoice();//识别内容
            JSONObject json = (JSONObject) JSONObject.toJSON(invoice);
            System.out.println(JSONObject.toJSONString(json));
            System.out.println(JSONObject.toJSONString(data.getDetail()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @GetMapping(value = "/getUser",produces = {"application/json;charset=UTF-8"})
    public ResultVO getUserInfo(){
        return ResultVO.success(getUser());
    }
}
