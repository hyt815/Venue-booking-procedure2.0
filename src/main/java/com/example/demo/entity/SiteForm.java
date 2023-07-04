package com.example.demo.entity;

/**
 * @author yxl
 * @date 2023/2/25 上午2:08
 */
public class SiteForm {
    private int site_id;
    private String weekday;
    private String venue;
    private String book_time;

    public SiteForm(int site_id, String weekday, String venue, String book_time) {
        this.site_id = site_id;
        this.weekday = weekday;
        this.venue = venue;
        this.book_time = book_time;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
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
