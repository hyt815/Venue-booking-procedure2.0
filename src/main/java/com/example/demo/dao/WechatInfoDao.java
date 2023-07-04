package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yxl
 * @date 2023/2/25 上午1:08
 */

@Component
public class WechatInfoDao {

    private final JdbcTemplate jdbcTemplate;

    public WechatInfoDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean userIsExist(String uid) {
        String sql = "select count(1) from wechat_info where user_id =?";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class, uid);
        return res != 0;
    }

    public void insertWechatInfo(String uid, String uname, String stu_num, String class_id) {
        String sql = "insert into wechat_info(user_id,uname,stu_num,class_id) values(?,?,?,?)";
        jdbcTemplate.update(sql, uid, uname, stu_num, class_id);
    }
}
