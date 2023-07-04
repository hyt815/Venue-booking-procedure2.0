package com.example.demo.entity;

/**
 * @author yxl
 * @date 2023/2/25 上午2:00
 */
public class AppInfo {
    private int id;
    private String time;
    private int status;
    private int place;
    private String date;

    public AppInfo(int id, String time, int status, int place, String date) {
        this.id = id;
        this.time = time;
        this.status = status;
        this.place = place;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
