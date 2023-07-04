package com.example.demo.dao;

import com.example.demo.entity.SiteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yxl
 * @date 2023/2/23 下午1:42
 */

@Component
public class SiteDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer findIdByWVB(String weekstips, String venueid, String time) {
        String sql = "select site_id from site_form where weekday=? and venue=? and book_time=?";

        return jdbcTemplate.queryForObject(sql, Integer.class, weekstips, venueid, time);
    }

    public void loadSiteData() {
        String sql = "insert into site_form(site_id,weekday,venue,book_time) values(?,?,?,?)";
        int sid = 1;
        for (int i = 0; i < 7; i++) {
            for (int j = 1; j <= 18; j++) {
                for (int k = 1; k <= 4; k++) {
                    jdbcTemplate.update(sql, sid++, week(i), String.valueOf(j), time(k));
                }
            }
        }
    }

    public void deleteAll() {
        String sql = "delete from site_form";
        jdbcTemplate.update(sql);
    }

    public void insertSite_form(int sid, int week, String venue, String time) {
        String sql = "insert into site_form(site_id,weekday,venue,book_time) values(?,?,?,?)";
        jdbcTemplate.update(sql, sid, week(week), venue, time);
    }

    public SiteForm findSiteByS(int sid) {
        String sql = "select * from site_form where site_id=?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(SiteForm.class), sid).get(0);
    }

    public int findDateCount(String now, int i) {
        String sql = "select count(1) from site_form where weekday=? and venue=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, now, String.valueOf(i));
    }


    public String week(int i) {
        switch (i) {
            case 0:
                return "星期日";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            default:
                return "";
        }
    }

    public String time(int k) {
        switch (k) {
            case 1:
                return "13:00-14:00";

            case 2:
                return "14:00-15:00";

            case 3:
                return "15:00-16:00";

            case 4:
                return "16:00-17:00";
            default:
                return "";
        }
    }


    public int findIdByWV(String weekDay, String s) {
        String sql = "select site_id from site_form where weekday=? and venue=?";
        return jdbcTemplate.queryForList(sql, Integer.class, weekDay, s).get(0);
    }
}
