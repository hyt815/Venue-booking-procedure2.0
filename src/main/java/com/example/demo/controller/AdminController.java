package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.AdminServiceImpl;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/admin")

public class AdminController {
/*
    private final AdminServiceImpl adminServiceImpl;

    public AdminController(AdminServiceImpl adminServiceImpl) {
        this.adminServiceImpl = adminServiceImpl;
    }

    //管理员登录
    @RequestMapping(value = "/adminRegister" ,method = RequestMethod.POST)
    @ResponseBody
    public Object adminRegister(@RequestParam("id") String id,
                                @RequestParam("pwd") String pwd){
        MyResponse myResponse=new MyResponse();
        if (adminServiceImpl.adminLogin(id, pwd)) {
            myResponse.setStatus(1);
        } else myResponse.setStatus(0);
        return myResponse;
    }

*//*    //签到
    @RequestMapping(value = "/statistics" ,method = RequestMethod.POST)
    @ResponseBody
    public Object statistics (@RequestParam("date") String date,
                              @RequestBody("no") String[] no){
        MyResponse myResponse=new MyResponse();
        JsonToTrray jsonToTrray=new JsonToTrray(no);
        String[] arr= jsonToTrray.getArray();
        if (adminServiceImpl.statistics(date,arr)) {
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);
        return myResponse;
    }*//*

    //签到
    @RequestMapping(value = "/statistics" ,method = RequestMethod.POST)
    @ResponseBody
    public Object statistics (@RequestBody Array array){
        MyResponse myResponse=new MyResponse();
        String[] strings=adminServiceImpl.statistics(array.getData(),array.getArray());
        if (strings[0].equals("2")){
            myResponse.setStatus(1); //所有id都存在
        }
        if (strings[0].equals("0")) {
            myResponse.setStatus(0);//所有id都不存在
        }
        if (strings[0].equals("1")){ //有的id存在，有的id不存在
            myResponse.setStatus(1);
            int len=strings.length-2;
            StringBuilder s= new StringBuilder();
            for (int i=1;i<len;i++){
                if (strings[i]!=null) {
                    s.append(strings[i]);
                    s.append("、");
                }
            }
            if (strings[len]!=null) {
                s.append(strings[len]);
            }
            myResponse.setData("这些学号不存在："+ s);
        }
        return myResponse;
    }

    //定位违约人员
    @RequestMapping(value = "/violation" ,method = RequestMethod.POST)
    @ResponseBody
    public Object violation (@RequestParam("id") String id){
        MyResponse myResponse=new MyResponse();
        Student student=adminServiceImpl.violation(id);
        if (student!=null){
            myResponse.setStatus(1);
            Gson gson = new Gson();
            myResponse.setData(gson.toJson(new MyData(student.getStudentID(),student.getName(),student.getBreach(),student.getRevoke_num())));
        }else myResponse.setStatus(0);
        return myResponse;
    }

    //解封
    @RequestMapping(value = "/unblock" ,method = RequestMethod.POST)
    @ResponseBody
    public Object unblock (@RequestParam("id") String id){
        MyResponse myResponse=new MyResponse();
        if (adminServiceImpl.unblock(id)){
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);
        return myResponse;
    }

    //将取消次数设为0
    @RequestMapping(value = "/unrevokenum" ,method = RequestMethod.POST)
    @ResponseBody
    public Object unrevokenum(@RequestParam("id") String id){
        MyResponse myResponse=new MyResponse();
        if (adminServiceImpl.un_revoke_num(id)){
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);
        return myResponse;
    }


    //输出某天预约人员信息
    @RequestMapping(value = "/surface" ,method = RequestMethod.POST)
    @ResponseBody
    public Object surface (@RequestParam("date") String date){
        MyResponse myResponse=new MyResponse();
        ArrayList<AppointmentInformation> array=adminServiceImpl.surface(date);
        if (array!=null) {
            myResponse.setStatus(1);
            Gson gson = new Gson();
            myResponse.setData(gson.toJson(array));
        }else myResponse.setStatus(0);
        return myResponse;
    }

    //登录校验
    @RequestMapping(value = "/test" ,method = RequestMethod.POST)
    @ResponseBody
    public Object test (@RequestParam("id") String id,
                        @RequestParam("str") String str){
        MyResponse myResponse=new MyResponse();
        myResponse.setStatus(adminServiceImpl.test(id,str));
        return myResponse;
    }*/
}
