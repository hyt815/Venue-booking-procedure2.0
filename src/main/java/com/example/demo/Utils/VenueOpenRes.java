package com.example.demo.Utils;

import com.example.demo.dao.AppDao;
import com.example.demo.dao.RedisUtil;
import com.example.demo.dao.SiteDao;
import com.example.demo.entity.AppInfoView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yxl
 * @date 2023/2/23 下午1:03
 */

@Component
public class VenueOpenRes {

    private final RedisUtil redisUtil;

    private final AppDao appDao;

    private final SiteDao siteDao;

    public VenueOpenRes(RedisUtil redisUtil, AppDao appDao, SiteDao siteDao) {
        this.redisUtil = redisUtil;
        this.appDao = appDao;
        this.siteDao = siteDao;
    }

    public String getVenueOpenClass() {
        JsonObject jsonObject = new JsonObject();
        Random r = new Random();
        JsonArray week = new JsonArray();
        JsonObject w;
        for (int x = 0; x < 7; x++) {
            w = new JsonObject();
            JsonObject v;
            JsonArray vv = new JsonArray();
            JsonArray status;
            String now = getWeekDay();
            int count = siteDao.findDateCount(now, 1);
            for (int y = 0; y < 18; y++) {
                status = new JsonArray();
                v = new JsonObject();
                //判断是否为当天
                if (now.equals(getWeekDay(x))) {
                    int index = siteDao.findIdByWV(getWeekDay(),"1");
                    for (int z = 0; z < count; z++) {
                        //从redis中获取数据，若数据存在 这说明有人预约 否则就没人预约
                        //System.out.println((index + y * count + z));
                        Object o = redisUtil.get("siteId:" + (index + y * count + z));
                        status.add(o == null ? 1 : 0);
                    }
                } else {//不是当天则直接全是1
                    for (int z = 0; z < count; z++) {
                        status.add(0);
                    }
                }
                v.add("venueid", status);
                vv.add(v);
            }
            w.add("weeksday", vv);
            week.add(w);
        }
        jsonObject.add("msg", week);
        return jsonObject.toString();
    }

    public String appointmentRecord(String uid) {
        List<AppInfoView> appInfoViews = appDao.selectAllByU(uid);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        JsonArray all = new JsonArray();
        JsonArray j1 = new JsonArray();
        JsonArray j2 = new JsonArray();
        JsonArray j3 = new JsonArray();
        Collections.sort(appInfoViews, new Comparator<AppInfoView>() {
            @Override
            public int compare(AppInfoView o1, AppInfoView o2) {
                return Long.compare(o2.getTime().getTime(), o1.getTime().getTime());
            }
        });
        int i = 0;
        for (AppInfoView aiv : appInfoViews) {
            if (i >= 30) {
                break;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", aiv.getAid());
            jsonObject.addProperty("time", aiv.getBook_time());
            jsonObject.addProperty("state", aiv.getStatus());
            jsonObject.addProperty("place", aiv.getVenue());
            jsonObject.addProperty("date", format.format(aiv.getTime().getTime()));
            all.add(jsonObject);
            switch (aiv.getStatus()) {
                case 1:
                    j1.add(jsonObject);
                    break;
                case 2:
                    j2.add(jsonObject);
                    break;
                case 3:
                    j3.add(jsonObject);
            }
            i++;
        }
        JsonArray res = new JsonArray();
        res.add(all);
        res.add(j1);
        res.add(j2);
        res.add(j3);
        return res.toString();
    }


    private int getSiteIndex(String week) {
        switch (week) {
            case "星期日":
                return 1;
            case "星期一":
                return 73;
            case "星期二":
                return 145;
            case "星期三":
                return 217;
            case "星期四":
                return 289;
            case "星期五":
                return 361;
            case "星期六":
                return 433;
        }
        return 1;
    }

    public String getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        String r = null;
        switch (i) {
            case 1:
                r = "星期日";
                break;
            case 2:
                r = "星期一";
                break;
            case 3:
                r = "星期二";
                break;
            case 4:
                r = "星期三";
                break;
            case 5:
                r = "星期四";
                break;
            case 6:
                r = "星期五";
                break;
            case 7:
                r = "星期六";
                break;
        }

        return r;
    }

    private String getWeekDay(int x) {
        String r = null;
        switch (x) {
            case 0:
                r = "星期日";
                break;
            case 1:
                r = "星期一";
                break;
            case 2:
                r = "星期二";
                break;
            case 3:
                r = "星期三";
                break;
            case 4:
                r = "星期四";
                break;
            case 5:
                r = "星期五";
                break;
            case 6:
                r = "星期六";
                break;
        }

        return r;
    }
}
