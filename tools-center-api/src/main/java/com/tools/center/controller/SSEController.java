package com.tools.center.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping(value = "/sse")
public class SSEController {

    @RequestMapping(value = "/sendMsg",produces = "text/event-stream;charset=UTF-8")
    public String sseSendMsg(){
//        resp.setContentType("text/event-stream;charset=utf-8");
//        PrintWriter out = null;
//        try {
//            out = resp.getWriter();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < 10; i++) {
//            out.write("data: 江南一点雨:" + i+"\n\n");
//            out.flush();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        return "data: 江南一点雨:" + "\n\n";
    }
}
