package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yxl
 * @date 2023/2/23 下午1:05
 */

@Component
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean findUserByNSC(String name, String id, String clazz) {
        String sql = "select count(1) from users where uname=? and student_id=? and class_id=?";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class, name, id, clazz);
        return res == 1;
    }

    public Integer findUidByS(String id) {
        String sql = "select uid from users where student_id=?";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return res;
    }

    public Integer findUidBySP(String id, String pwd) {
        String sql = "select uid from users where student_id=? and upwd=?";
        Integer res = jdbcTemplate.queryForObject(sql, Integer.class, id, pwd);
        return res;
    }

    public int insertUser(String name, String sid, String pwd, String cid, byte[] icon) {
        String sql = "insert into users(uname,student_id,upwd,class_id,Icon) values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, name, sid, pwd, cid, icon);
    }


    public int updatePwdByS(String sid, String pwd) {
        String sql = "update users set upwd=? where student_id=?";
        int update = jdbcTemplate.update(sql, pwd, sid);
        return update;
    }

    public byte[] findUrlByS(String sid) {
        String sql = "select Icon from users where student_id=?";
        return jdbcTemplate.queryForObject(sql, byte[].class, sid);
    }
}
