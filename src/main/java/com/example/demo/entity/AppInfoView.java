package com.example.demo.entity;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * @author yxl
 * @date 2023/2/25 上午2:02
 */
public class AppInfoView {
    private int aid;
    private int site_id;
    private String uid;
    private Timestamp time;
    private int status;

    private String venue;
    private String book_time;

    public AppInfoView(int aid, int site_id, String uid, Timestamp time, int status, String venue, String book_time) {
        this.aid = aid;
        this.site_id = site_id;
        this.uid = uid;
        this.time = time;
        this.status = status;
        this.venue = venue;
        this.book_time = book_time;
    }

    public AppInfoView() {

    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getBook_time() {
        return book_time;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }
}
