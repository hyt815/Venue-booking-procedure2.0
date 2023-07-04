package com.example.demo.schedule;

import com.example.demo.dao.AppDao;
import com.example.demo.dao.RedisUtil;
import com.example.demo.dao.SiteDao;
import com.example.demo.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yxl
 * @date 2023/2/25 上午2:37
 */

@Component
public class Schedule {

    private final RedisUtil redisUtil;

    private final AppDao appDao;

    private final SiteDao siteDao;

    private final List<String> siteData;

    private AtomicBoolean flag;

    private final AtomicInteger f;

    private final String[] wg;

    private final StudentServiceImpl service;

    public Schedule(RedisUtil redisUtil, AppDao appDao, SiteDao siteDao, List<String> siteData, AtomicBoolean flag, AtomicInteger f, String[] wg, StudentServiceImpl service) {
        this.redisUtil = redisUtil;
        this.appDao = appDao;
        this.siteDao = siteDao;
        this.siteData = siteData;
        this.flag = flag;
        this.f = f;

        this.wg = wg;
        this.service = service;
    }

    /**
     * 将更改当天未履行的状态
     */
    //每天凌晨0点执行
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void loadAppInfo() {
        List<Integer> aids = appDao.findAByStatus(2);
        for (int a : aids) {
            appDao.updateStatusByA(a, 3);
        }
        System.out.println("我进来了 loadAppInfo");
    }

    /**
     * 将更改当天未履行的状态
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void deleteRedis() {
        redisUtil.flushAll();
        System.out.println("我进来了 deleteRedis");
    }

    /**
     * 刷新第二天的site配置库
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void updateSiteData() {
        if (flag.get()) {
            siteDao.deleteAll();
            for (String s : siteData) {
                String[] date = s.split("_");
                siteDao.insertSite_form(Integer.parseInt(date[0]), Integer.parseInt(date[1]), date[2], date[3]);
            }
            siteData.clear();
            flag.set(false);
        }
        System.out.println("我进来了 updateSiteData");
    }

    @Scheduled(cron = "0 1 0 * * ?")
    @Async
    public void wai_gua() {
        if (f.get() != 0) {
            service.booking(wg[0], wg[1], wg[2]);
            f.set(0);
        }
    }
}
