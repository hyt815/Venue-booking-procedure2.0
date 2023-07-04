package com.example.demo.schedule;

import com.example.demo.dao.AppDao;
import com.example.demo.dao.RedisUtil;

import java.util.TimerTask;

/**
 * @author yxl
 * @date 2023/3/18 下午10:32
 */
public class MyTask extends TimerTask {

    private final RedisUtil redisUtil;
    private final String uid;
    private final AppDao appDao;

    public MyTask(RedisUtil redisUtil, String uid, AppDao appDao) {
        this.redisUtil = redisUtil;
        this.uid = uid;
        this.appDao = appDao;
    }

    @Override
    public void run() {
        Integer status = (Integer) redisUtil.get("uid:" + uid);
        if (status == 2) {
            redisUtil.set("uid:" + uid, 3);
            appDao.updateStatusByUS(3, uid, 2);
        }
        this.cancel();
    }
}
