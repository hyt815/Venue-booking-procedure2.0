package com.example.demo.service;

import com.example.demo.Utils.JWTUtil;
import com.example.demo.Utils.VenueOpenRes;
import com.example.demo.dao.*;
import com.example.demo.entity.MyResponse;
import com.example.demo.entity.SiteForm;
import com.example.demo.schedule.MyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StudentServiceImpl {

    private final UserDao userDao;

    private final SiteDao siteDao;

    private final AppDao appDao;

    private final RedisUtil redisUtil;

    private final VenueOpenRes venueOpenRes;

    private final WechatInfoDao wechatInfoDao;

    private final List<String> siteData;

    private final AtomicBoolean flag;

    private final Timer timer;

    private final AtomicInteger f;

    private final String[] wg;

    public StudentServiceImpl(UserDao userDao, SiteDao siteDao, RedisUtil redisUtil, AppDao appDao,
                              VenueOpenRes venueOpenRes, WechatInfoDao wechatInfoDao, List<String> siteData, AtomicBoolean flag, Timer timer, AtomicInteger f, String[] wg) {
        this.userDao = userDao;
        this.siteDao = siteDao;
        this.redisUtil = redisUtil;
        this.appDao = appDao;
        this.venueOpenRes = venueOpenRes;
        this.wechatInfoDao = wechatInfoDao;
        this.siteData = siteData;
        this.flag = flag;
        this.timer = timer;
        this.f = f;
        this.wg = wg;
    }


    public void loadDefaultSiteData() {
        siteDao.loadSiteData();
    }

    public MyResponse updateSiteData(String[] dates) {
        siteData.clear();
        int sid = 1;
        for (int i = 0; i < 7; i++) {
            if ("0".equals(dates[i])) {
                continue;
            }
            for (int j = 1; j <= 18; j++) {
                for (String t : getDate(dates[i])) {
                    siteData.add(sid++ + "_" + i + "_" + j + "_" + t);
//                    siteDao.insertSite_form(sid++, i, String.valueOf(j), t);
                }
            }
        }
        flag.set(true);
        return new MyResponse(1);
    }

    public MyResponse updateSiteData_1(String[] dates) {
        siteDao.deleteAll();
        int sid = 1;
        for (int i = 0; i < 7; i++) {
            if ("0".equals(dates[i])) {
                continue;
            }
            for (int j = 1; j <= 18; j++) {
                for (String t : getDate(dates[i])) {
//                    siteData.add(sid++ + "_" + i + "_" + j + "_" + t);
                    siteDao.insertSite_form(sid++, i, String.valueOf(j), t);
                }
            }
        }
        return new MyResponse(1);
    }

    public List<String> getDate(String date) {
        List<String> res = new ArrayList<>();
        String[] d = date.split("\\|");
        for (String s : d) {
            String[] split = s.split("-");
            int len = split.length;
            for (int i = 0; i < len; i += 2) {
                String[] s1 = split[i].split(":");
                String[] s2 = split[i + 1].split(":");
                int start = Integer.parseInt(s1[0]);
                int end = Integer.parseInt(s2[0]);
                for (int j = start; j < end; j++) {
                    res.add((j < 10 ? "0" + j : j) + ":" + s1[1] + "-" + ((j + 1) < 10 ? "0" + (j + 1) : (j + 1)) + ":" + s2[1]);
                }
            }
        }
        return res;
    }


    public MyResponse wai_gua(String venueid, String time, String uid) {
        if (f.get() == 0) {
            wg[0] = venueid;
            wg[1] = time;
            wg[2] = uid;
            f.set(1);
        }
        return new MyResponse(1);
    }

    /**
     * 1、先判断用户都注册过
     * 2、获取到场地配置表中的场地id
     * 3、到redis中查询场地是否被预约(直接setnx 相当于加锁)    redis key：场地id value 学生学号
     * 4.1、若被预约返回0
     * 4.2、若没有被预约将预约信息如mysql库
     * 5、将预约信息存入redis 及预约信息 和状态信息  状态信息  key：预约记录id  value：状态 0 1 31 32
     * 6、返回
     */
    public MyResponse booking(String venueid, String time, String uid) {
        String week = venueOpenRes.getWeekDay();
        Object o = redisUtil.get("uid:" + uid);
        if (o != null) {
            //若这个用户今天已经预约过则返回1
            return new MyResponse(1);
        }
        //判断是不是被抢注册
        Integer siteId = siteDao.findIdByWVB(week, venueid, time);
        if (siteId == null) {
            return new MyResponse(-1);
        }
        //成功了代表没人用，没成功代表有人预约了
        boolean ok = redisUtil.setnx("siteId:" + siteId, uid);
        if (!ok) {
            //被抢预约返回0
            return new MyResponse(0);
        }
        //获取uid
        redisUtil.set("uid:" + uid, 2);
        //记录入库
        appDao.insertApp_Info(siteId, uid, 2, venueid, time);

        //设置定时器任务
        String[] tt = time.split("-")[0].split(":");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tt[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(tt[1]));
        c.add(Calendar.MINUTE, 15);
        timer.schedule(new MyTask(redisUtil, uid, appDao), c.getTime());
        //成功返回2
        return new MyResponse(2);
    }


    public MyResponse register(String name, String sid, String pwd, String cid, byte[] icon) {
        Integer uidByS = userDao.findUidByS(sid);
        if (uidByS != null) {
            return new MyResponse(0);
        }
        int b = userDao.insertUser(name, sid, pwd, cid, icon);
        return new MyResponse(b);
    }

    public MyResponse login(String sid, String pwd) {
        Integer uidBySP = userDao.findUidBySP(sid, pwd);
        if (uidBySP == null) {
            return new MyResponse(0);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(uidBySP));
        map.put("student_id", sid);
        map.put("password", pwd);
        String token = JWTUtil.GenerateToken(map);
        return new MyResponse(1, token);
    }

    public MyResponse changePassword(String sid, String pwd) {
        int i = userDao.updatePwdByS(sid, pwd);
        return new MyResponse(i);
    }

    public MyResponse userInformation(String sid) {
        byte[] data = userDao.findUrlByS(sid);
        return new MyResponse(data);
    }

    public MyResponse verify(Integer site, String date) {
        //获取今天星期几
        String weekDay = venueOpenRes.getWeekDay();
        //获取场地id
        Integer siteId = siteDao.findIdByWVB(weekDay, String.valueOf(site), date);
        //判断场地是否有人预约
        Object o = redisUtil.get("siteId:" + siteId);

        String[] d = date.split("-")[0].split(":");

        Calendar book = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        book.set(Calendar.HOUR_OF_DAY, Integer.parseInt(d[0]));
        book.set(Calendar.MINUTE, Integer.parseInt(d[1]));
        //当前时间大于预约时间
        boolean b = now.getTimeInMillis() >= book.getTimeInMillis();
        return new MyResponse(o == null && !b ? 1 : 0);
    }

    public MyResponse cancelReservation(String uid) {
        //判断用户今天有没有预约
        Object o = redisUtil.get("uid:" + uid);
        if (o == null) {
            //该用户今天没有预约
            return new MyResponse(0);
        }
        //判断用户当前预约的状态
        Integer i = (Integer) o;
        if (i != 2) {
            //返回-1 代表该用户已履行无法取消
            return new MyResponse(-1);
        }
        //获取场地id
        Integer siteId = appDao.findSidByUS(uid, 2);
        //删redis缓存
        redisUtil.del("siteId:" + siteId, "uid:" + uid);
        //改库
        appDao.updateStatusByUS(-1, uid, 2);
        return new MyResponse(1);
    }

    public MyResponse signIn(String uid, Integer site_id) {
        System.out.println("进来了");
        Integer siteId = appDao.findSidByUS(uid, 2);
        Object o = redisUtil.get("siteId:" + siteId);
        if (!uid.equals(o)) {
            //该场地不是这个用户预约的
            System.out.println("进第一个了");
            return new MyResponse(0);
        }
        String site = appDao.selectVByUS(uid, 2);
        System.out.println("Integer.parseInt(site.getVenue())=" + Integer.parseInt(site));
        System.out.println("site_id=" + site_id);
        System.out.println("siteId=" + siteId);
        System.out.println(Integer.parseInt(site) != site_id);
        if (Integer.parseInt(site) != site_id) {
            //该场地不是这个用户预约的
            return new MyResponse(0);
        }
        Integer status = (Integer) redisUtil.get("uid:" + uid);
        if (status.equals(1)) {
            //-1表示已经签到
            return new MyResponse(-1);
        } else if (status.equals(3)) {
            //-2表示已经超时
            return new MyResponse(-2);
        }
        //将该用户状态改成已预约
        redisUtil.set("uid:" + uid, 1);
        //改库
        appDao.updateStatusByUS(1, uid, 2);
        return new MyResponse(1);
    }

    public MyResponse verify_user_one(String uid) {
        boolean b = wechatInfoDao.userIsExist(uid);
        return new MyResponse(b ? 1 : 0);
    }

    public MyResponse verify_user_two(String uid, String name, String stu_num) {
        wechatInfoDao.insertWechatInfo(uid, name, stu_num, "class");
        return new MyResponse(1);
    }

    public int updateAllStatus() {
        return appDao.updateStatusByU(2);
    }
}