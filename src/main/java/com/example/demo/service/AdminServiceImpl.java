package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl {
/*

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Resource
    private RedisTemplate<String, AppInfo> redisTemplate;

    //管理员登录
    public boolean adminLogin(String id, String pwd) {
        if (id == null || pwd == null) return false;

        //验证用户名和密码正确性；
        List<String> pwddList = jdbcTemplate.queryForList("select aPwd from admin_info where adminID=?", String.class, id);
        if (pwddList.size() == 0 || !pwddList.get(0).equals(pwd)) return false;

        //将数据库“管理人员信息”中登陆状态置为logIn并返回1
        int res = jdbcTemplate.update("update admin_info set state='logIn' where adminID=?", id);
        if (res == 0) {
            return false;
        } else return true;
    }

    //签到
    public String[] statistics(String date, String[] no) {

        String sql1 = "select breach from student_info where studentID=?";

        int noLength = no.length;
        String[] strings = new String[noLength + 1];  //第一位存放0/1/2 后面的用来存放不存在的学号
        int n = 1;
        for (String s : no) {
            List<Integer> breachList = jdbcTemplate.queryForList(sql1, Integer.class, s);
            if (breachList.size() == 0) {
                strings[n] = s;
                n += 1;
            }
        }
        if (n - 1 == noLength) {
            strings[0] = "0";//所有id都不存在，0
            return strings;//直接返回
        } else if (n == 1) {
            strings[0] = "2";//所有id都存在，2
        } else {
            strings[0] = "1";//只要有id存在,1
            return strings;//直接返回
        }


        //根据传入的日期找到数据库中“预约信息”wait部分得到用户预约编号
        List<Integer> reserveIdlist = jdbcTemplate.queryForList("select reserveID from app_info where date=? and state='wait'", Integer.class, date);

        //根据用户预约编号将数据库“场预约情况”对应部分一一删除
        int reserveIdArrayLength = reserveIdlist.size();
        for (Integer integer : reserveIdlist) {
            jdbcTemplate.update("delete from list_site where reserveID=?", integer);
        }

        //再根据学号（违约人员）和日期和数据库中“预约信息”wait部分，依次将违约人员状态设置为breach,并将学生信息表中违约次数加一
        String sql = "update app_info set state='breach' where studentID=? and date =? and state='wait'";
        String sql2 = "update student_info set breach=? where studentID=?";

        for (String s : no) {
            List<Integer> breachList = jdbcTemplate.queryForList(sql1, Integer.class, s);
            int breach = breachList.get(0);
            jdbcTemplate.update(sql2, ++breach, s);
            jdbcTemplate.update(sql, s, date);
        }

        //再将其他人设置为finish
        for (int i = 0; i < reserveIdArrayLength - noLength; i++) {
            jdbcTemplate.update("update app_info set state='finish' where date =? and state='wait'", date);
        }

        //根据学号更新redis
        String sql3 = "select * from app_info where reserveID=?";
        for (Integer integer : reserveIdlist) {
            AppInfo appInfo = jdbcTemplate.queryForObject(sql3, new BeanPropertyRowMapper<>(AppInfo.class), integer);
            if (appInfo != null) {
                redisTemplate.opsForValue().set(appInfo.getStudentID(), appInfo);
            }
        }
        return strings;
    }

    //定位违约人员
    public Student violation(String id) {
        if (id == null) return null;
        //根据学号在数据库“学生信息”验证他是否3次违约
        String sql = "select breach from student_info where studentID=?";
        List<Integer> breachList = jdbcTemplate.queryForList(sql, Integer.class, id);
        //没有——返回0
        if (breachList.size() == 0) {
            return null;
        } else {
            //有——返回学号和姓名
            String sql1 = "select * from student_info where studentID=?";
            return jdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<>(Student.class), id);
        }
    }

    //解封
    public boolean unblock(String id) {
        if (id == null) return false;
        //根据学号在数据库“学生信息”中将违约次数置为0
        int a = jdbcTemplate.update("update student_info set breach=0 where studentID=?", id);
        return a != 0;

    }

    //将取消次数归零
    public boolean un_revoke_num(String id) {
        if (id == null) return false;

        //根据学号在数据库“学生信息”中将取消次数置为0
        int a = jdbcTemplate.update("update student_info set revoke_num=0 where studentID=?", id);
        return a != 0;
    }

    //输出某天预约人员信息
    public ArrayList<AppointmentInformation> surface(String date) {
        //根据日期在数据库“预约信息”中得到用户预约编号
        List<Integer> reserveIdList = jdbcTemplate.queryForList("select reserveId from app_info where date=?", Integer.class, date);

        ArrayList<AppointmentInformation> arrayList = new ArrayList<>();

        for (int s : reserveIdList) {
            //在“预约信息”中根据用户预约编号得到学号
            List<String> studentIdList = jdbcTemplate.queryForList("select studentID from app_info where reserveId=?", String.class, s);
            if (studentIdList.size() == 0) return null;

            // 姓名（在“学生信息”中根据学号找到姓名）
            List<String> nameList = jdbcTemplate.queryForList("select name from student_info where studentID=?", String.class, studentIdList.get(0));
            if (nameList.size() == 0) return null;

            //在预约信息表中查询所有信息
            String sql1 = "select * from app_info where reserveID=?";
            AppInfo appInfo = jdbcTemplate.queryForObject(sql1, new BeanPropertyRowMapper<>(AppInfo.class), reserveIdList.get(0));


            // +时间
            //将查到的数据放到数组中
            arrayList.add(new AppointmentInformation(nameList.get(0), studentIdList.get(0), appInfo.getSite(), appInfo.getDir(), appInfo.getTime(), appInfo.getState()));
        }

        return arrayList;
    }

    //登录校验
    public int test(String id, String str) {
        //根据输入的用户名在数据库“管理人员信息”中获取管理员登录状态
        List<String> stateList = jdbcTemplate.queryForList("select state from admin_info where adminID=?", String.class, id);
        if (stateList.size() == 0) {
            if (str.equals("check")) {
                return 1;
            } else return 0;

        }

        int result = 0;
        //如果参数为check：
        if (str.equals("check")) {
            //检查登录状态 如果状态为logIn则输出0；offline输出1；
            if (stateList.get(0).equals("offline")) result = 1;
        }

        //如果参数exit：
        else if (str.equals("exit")) {
            //将登录状态置为 offline
            int a = jdbcTemplate.update("update admin_info set state='offline' where adminID=?", id);
            if (a != 0) {
                result = 1;
            } else result = 0;
        }
        return result;
    }
*/

    //批量修改某星期某块场地是否开放
}
