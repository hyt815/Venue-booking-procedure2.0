package com.example.demo.dao;

import com.example.demo.entity.AppInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yxl
 * @date 2023/2/23 下午2:46
 */

@Component
public class AppDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertApp_Info(int site_id, String uid, int status, String venue, String book) {
        String sql = "insert into app_info(site_id,uid,time,status,venue,book_time) values(?,?,?,?,?,?)";
        int update = jdbcTemplate.update(sql, site_id, uid, new Timestamp(System.currentTimeMillis()), status,
                venue, book);
        return update == 1;
    }

    public int findAidBySUS(int site_id, String uid, int status) {
        String sql = "select aid from app_info where site_id=? and uid=? and status=?";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, site_id, uid, status);
        return integer != null ? integer : -1;
    }

    public Integer findSidByUS(String uid, int status) {
        String sql = "select site_id from app_info where uid=? and status=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, uid, status);
    }

    public void updateStatusByUS(int i, String uid, int i1) {
        String sql = "update app_info set status=? where uid=? and status=?";
        jdbcTemplate.update(sql, i, uid, i1);
    }

    public void updateStatusByA(int aid, int status) {
        String sql = "update app_info set status=? where aid=?";
        jdbcTemplate.update(sql, status, aid);
    }

    public List<AppInfoView> selectAllByU(String uid) {
        String sql = "select * from app_info where uid=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(AppInfoView.class), uid);
    }

    public String selectVByUS(String uid, int status) {
        String sql = "select venue from app_info where uid=? and status=?";
        return jdbcTemplate.queryForObject(sql, String.class, uid, status);
    }

    public List<Integer> findAByStatus(int i) {
        String sql = "select aid from app_info where status=?";
        return jdbcTemplate.queryForList(sql, Integer.class, i);
    }

    public int updateStatusByU(int i) {
        String sql = "update app_info set status=-1 where status=?";
        int update = jdbcTemplate.update(sql, i);
        return update;
    }
}
