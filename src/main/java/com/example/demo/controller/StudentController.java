package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Utils.JWTUtil;
import com.example.demo.Utils.VenueOpenRes;
import com.example.demo.annotation.Token;
import com.example.demo.dao.RedisUtil;
import com.example.demo.entity.MyResponse;
import com.example.demo.service.StudentServiceImpl;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    private final VenueOpenRes venueOpenRes;

    private final RedisUtil redisUtil;


    public StudentController(StudentServiceImpl studentServiceImpl, VenueOpenRes venueOpenRes, RedisUtil redisUtil) {
        this.studentServiceImpl = studentServiceImpl;
        this.venueOpenRes = venueOpenRes;
        this.redisUtil = redisUtil;
    }

    //测试
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "hello world";
    }


    @GetMapping("/testRedis")
    @ResponseBody
    public String testRedis() {
        redisUtil.flushAll();
        return "flush ok";
    }

    @GetMapping("/updateAllStatus")
    @ResponseBody
    public String updateAllStatus() {
        int i = studentServiceImpl.updateAllStatus();
        return "update ok:" + i;
    }


    /**
     * 初始化场地数据 无需访问
     */
    @GetMapping("/load")
    @ResponseBody
    public void loadSiteData(@RequestParam("dates") String[] dates) {
        studentServiceImpl.updateSiteData_1(dates);
    }

    @GetMapping("/updateSite")
    @ResponseBody
    public MyResponse updateSite(@RequestParam("dates") String[] dates) {
        return studentServiceImpl.updateSiteData(dates);
    }

    /**
     * 弃用
     */
    @PostMapping("/register")
    @ResponseBody
    public MyResponse register(@RequestParam("name") String name, @RequestParam("student_id") String sid,
                               @RequestParam("password") String pwd, @RequestParam("class_id") String cid,
                               @RequestParam("Icon") byte[] icon) {
        return studentServiceImpl.register(name, sid, pwd, cid, icon);
    }

    /**
     * 弃用
     */
    @PostMapping("/login")
    @ResponseBody
    public MyResponse login(@RequestParam("student_id") String sid, @RequestParam("password") String pwd) {
        return studentServiceImpl.login(sid, pwd);
    }

    /**
     * 弃用
     */
    @PostMapping("/changePassword")
    @ResponseBody
    @Token
    public MyResponse changePassword(@RequestParam("student_id") String sid, @RequestParam("password") String pwd) {
        return studentServiceImpl.changePassword(sid, pwd);
    }

    /**
     * 弃用
     */
    @PostMapping("/userInformation")
    @ResponseBody
    public MyResponse userInformation(@RequestParam("student_id") String sid) {
        return studentServiceImpl.userInformation(sid);
    }

    @PostMapping("/verify_user_one")
    @ResponseBody
    public MyResponse verify_user_one(@RequestParam("user_unique_id") String uid) {
        return studentServiceImpl.verify_user_one(uid);
    }

    @PostMapping("/verify_user_two")
    @ResponseBody
    public MyResponse verify_user_two(@RequestParam("user_unique_id") String uid,
                                      @RequestParam("student_name") String name,
                                      @RequestParam("student_number") String stu_num) {
        return studentServiceImpl.verify_user_two(uid, name, stu_num);
    }

    @PostMapping("/verify")
    @ResponseBody
    public MyResponse verify(@RequestParam("site") Integer site, @RequestParam("date") String date) {
        return studentServiceImpl.verify(site, date);
    }

    @PostMapping("/booking")
    @ResponseBody
    public MyResponse booking(@RequestParam("site") String venueid, @RequestParam("date") String time,
                              @RequestParam("user_unique_id") String uid) {
        return studentServiceImpl.booking(venueid, time, uid);
    }

    @PostMapping("/wai_gua")
    @ResponseBody
    public MyResponse wai_gua(@RequestParam("site") String venueid, @RequestParam("date") String time,
                              @RequestParam("user_unique_id") String uid) {
        return studentServiceImpl.wai_gua(venueid, time, uid);
    }



    @ResponseBody
    @RequestMapping(value = "/wxapi/decryptCode", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public MyResponse decodeOpenid(HttpServletResponse response, String code) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("utf-8");

        String wxspAppid = "wx1880e6ef519f0b31";
        String wxspSecret = "1aba6444dce06a0caaebd336ace8b210";

        try {
            Map<String, String> map = new HashMap<>();
            // 授权（必填）
            String grant_type = "authorization_code";
            // 请求参数
            String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
                    + grant_type;
            System.out.println("解析code请求参数：" + params.toString());
            // 发送请求
            String sr = sendPost("https://api.weixin.qq.com/sns/jscode2session", params);
            // 解析相应内容（转换成json对象）
            JSONObject json = JSONObject.parseObject(sr);
            System.out.println("解析code请求结果:" + json.toString());
            // 获取会话密钥（session_key）
            String session_key = json.getString("session_key");
            String openid = json.getString("openid");

            System.out.println("openid:" + openid);
            return new MyResponse(1, openid);
        } catch (Exception e) {
            e.printStackTrace();
            return new MyResponse(0);
        }
    }



    @PostMapping("/getVenueOpen")
    @ResponseBody
    public String getVenueOpen() {
        return venueOpenRes.getVenueOpenClass();
    }

    @PostMapping("/cancelReservation")
    @ResponseBody
    public MyResponse cancelReservation(@RequestParam("user_unique_id") String uid) {
        return studentServiceImpl.cancelReservation(uid);
    }

    @PostMapping("/signIn")
    @ResponseBody
    public MyResponse signIn(@RequestParam("user_unique_id") String uid, @RequestParam("site _number") Integer site_id) {
        return studentServiceImpl.signIn(uid, site_id);
    }

    @PostMapping("/appointmentRecord")
    @ResponseBody
    public String appointmentRecord(@RequestParam("user_unique_id") String uid) {
        return venueOpenRes.appointmentRecord(uid);
    }



    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

/*
    //登录
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam("id") String id, @RequestParam("pwd") String pwd) {
        MyResponse myResponse = new MyResponse();
        if (studentServiceImpl.login(id,pwd)) {
            Map<String, String> claimMap = new HashMap<>();
            claimMap.put("id", id);

            myResponse.setStatus(1);
            myResponse.setData(JWTUtil.GenerateToken(claimMap));
//            System.out.println(myResponse.getData());
        } else myResponse.setStatus(0);

        return myResponse;
    }

    //修改密码
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    @ResponseBody
    @Token
    public Object changePassword(@RequestParam("id") String id,
                                 @RequestParam("pwd") String pwd) {
        MyResponse myResponse = new MyResponse();
        if (studentServiceImpl.changePassword(id,pwd)) {
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);
        return myResponse;
    }

    //查看预约
    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    @ResponseBody
    public Object viewAppointment(@RequestParam("id") String id) {
        MyResponse myResponse = new MyResponse();
        List<Map<String, Object>> list=studentServiceImpl.viewAppointment(id);
        if (list==null){
            myResponse.setStatus(0);
            return myResponse;
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        myResponse.setStatus(1);
        myResponse.setData(gson.toJson(list));
        return myResponse;
    }

    //取消预约
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    @Token
    public Object cancelReservation(@RequestParam("deviceId") String deviceId) {
        MyResponse myResponse = new MyResponse();
        Map<String,Object> map=studentServiceImpl.cancelReservation(deviceId);
        if (map==null){
            myResponse.setStatus(0);
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        myResponse.setStatus(1);
        myResponse.setData(gson.toJson(map));
        return myResponse;
    }

    //校验
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @ResponseBody
    @Token
    public Object verify(@RequestParam("site") Integer site,
                         @RequestParam("dir") String dir,
                         @RequestParam("date") String date,
                         @RequestParam("time") String time) {
        MyResponse myResponse = new MyResponse();
        if (studentServiceImpl.verify(site,dir,date,time)){
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);

        return myResponse;
    }

    //代预约
    @RequestMapping(value = "/finishReplace", method = RequestMethod.POST)
    @ResponseBody
    @Token
    public Object finishReplace(@RequestParam("name") String name,
                                @RequestParam("id") String id,
                                @RequestParam("clazz") String clazz,
                                @RequestParam("phone") String phone,
                                @RequestParam("site") Integer site,
                                @RequestParam("dir") String dir,
                                @RequestParam("date") String date,
                                @RequestParam("time") String time) {
        MyResponse myResponse = new MyResponse();
        if (studentServiceImpl.finishReplace(name,id,clazz,phone,site,dir,date,time)){
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);

        return myResponse;
    }

    //自己预约
    @RequestMapping(value = "/finishMyself", method = RequestMethod.POST)
    @ResponseBody
    @Token
    public Object finishMyself(@RequestParam("id") String id,
                               @RequestParam("site") Integer site,
                               @RequestParam("dir") String dir,
                               @RequestParam("date") String date,
                               @RequestParam("time") String time) {
        MyResponse myResponse = new MyResponse();
        if (studentServiceImpl.finishMyself(id, site, dir, date, time)) {
            myResponse.setStatus(1);
        }else myResponse.setStatus(0);

        return myResponse;
    }*/
}
