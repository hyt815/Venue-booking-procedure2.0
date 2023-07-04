package com.example.demo;

import com.example.demo.controller.AdminController;
import com.example.demo.controller.StudentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcOperations;

import javax.annotation.Resource;

@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private StudentController student;

    @Autowired
    private AdminController admin;

    @Autowired
    private JdbcOperations jdbcTemplate;

/*
    @Autowired
    private MysqlUtils mysqlUtils;


    @Resource
    private RedisTemplate<String, AppInfo> redisTemplate;
*/

//    @Test
//    public void mysqlcreate() {
////        String[] time = {"13:30-14:30", "14:30-15:30", "17:20-18:20", "18:20-19:20"};
////        String[] weeks = {"星期一", "星期二", "星期三", "星期四", "星期五"};
//
////        场地
////        for (int i = 1; i < 9; i++) {
//            mysqlUtils.siteFormInsert(i, "A");
//            mysqlUtils.siteFormInsert(i, "B");
////            mysqlUtils.siteFormInsert(i, "C");
////            mysqlUtils.siteFormInsert(i, "D");
////
////        }
////        周一场地是否可以预约
////        for (int i = 1; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期一", 0);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期一", 0);
////            mysqlUtils.sessionFormInsert(i, "17:20-18:20", "星期一", 1);
////            mysqlUtils.sessionFormInsert(i, "18:20-19:20", "星期一", 1);
////        }
////
//////        周二场地是否可以预约
////        for (int i = 1; i < 5; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期二", 1);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期二", 1);
////        }
////        for (int i = 5; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期二", 0);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期二", 0);
////        }
////        for (int i = 1; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "17:20-18:20", "星期二", 0);
////            mysqlUtils.sessionFormInsert(i, "18:20-19:20", "星期二", 0);
////        }
////
//////        周三场地是否可以预约
////        for (int i = 1; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期三", 0);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期三", 0);
////            mysqlUtils.sessionFormInsert(i, "17:20-18:20", "星期三", 1);
////            mysqlUtils.sessionFormInsert(i, "18:20-19:20", "星期三", 1);
////        }
////
//////        周四场地是否可以预约
////        for (int i = 1; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期四", 0);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期四", 0);
////            mysqlUtils.sessionFormInsert(i, "17:20-18:20", "星期四", 1);
////            mysqlUtils.sessionFormInsert(i, "18:20-19:20", "星期四", 1);
////        }
////
//////        周五场地是否可以预约
////        for (int i = 1; i < 5; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期五", 1);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期五", 1);
////        }
////        for (int i = 5; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "13:30-14:30", "星期五", 0);
////            mysqlUtils.sessionFormInsert(i, "14:30-15:30", "星期五", 0);
////        }
////        for (int i = 1; i < 9; i++) {
////            mysqlUtils.sessionFormInsert(i, "17:20-18:20", "星期五", 0);
////            mysqlUtils.sessionFormInsert(i, "18:20-19:20", "星期五", 0);
////        }
//
//    }

//    @Test
//    public void text() {
//        System.out.println("签到");
//        check(admin.statistics(new Array("2022-09-28", new String[]{"0303200206", "123"})));
//
//    }

/*    @Test
    public void studentcheck() {
        System.out.print("登录");
        check(student.login("0303200206", "12345678"));

        System.out.print("修改密码");
        check(student.changePassword("0303200206", "12345678"));

        System.out.print("校验");
        check(student.verify(1, "A", "2022-03-04", "13:30-14:30"));

        System.out.print("自己预约");
        check(student.finishMyself("0303200206", 1, "A", "2022-03-04", "13:30-14:30"));

        System.out.print("校验");
        check(student.verify(1, "A", "2022-03-04", "13:30-14:30"));

        System.out.print("查看预约");
        check(student.viewAppointment("0303200206"));

        System.out.print("取消预约");
        check(student.cancelReservation("0303200206"));

        System.out.print("校验");
        check(student.verify(1, "A", "2022-03-04", "13:30-14:30"));

        System.out.print("代预约");
        check(student.finishReplace("小明", "0303200206", "网络01", "13835004191", 1, "A", "2022-03-04", "13:30-14:30"));
        System.out.print("校验");
        check(student.verify(1, "A", "2022-03-04", "13:30-14:30"));

        //        System.out.println("代预约取消");
//        check(student.cancelReservation("0303200206"));
    }

    @Test
    public void admincheck() {
        System.out.print("管理员登录");
        check(admin.adminRegister("123456", "123456"));

        System.out.print("输出某天预约人员信息");
        check(admin.surface("2022-03-04"));

*//*        System.out.print("签到");
        array[] id = new array[1];
        id[0] = new array("id","0303200206");
        Gson gson = new Gson();
        System.out.println(gson.toJson(id));
        check(admin.statistics("2022-03-04", gson.toJson(id)));*//*
        System.out.println("签到");
        check(admin.statistics(new Array("2022-09-28", new String[]{"123", "123456"})));
        System.out.print("定位违约人员");
        check(admin.violation("030320020"));

//        System.out.print("解封");
//        check(admin.unblock("0303200206"));


        System.out.print("登录校验");
        check(admin.test("123456", "exit"));

    }

    public void check(Object b) {
        MyResponse myResponse = (MyResponse) b;
        System.out.print("statues:" + myResponse.getStatus() + "\t");
        if (myResponse.getStatus() == 1) {
            System.out.println("成功！\n" + myResponse.getData() + "\n" + "=============================");
        } else if (myResponse.getStatus() == 0)
            System.out.println("失败！\n" + myResponse.getData() + "\n=======================");
    }*/

}
