package com.example.demo.entity;

/**
 * @author yxl
 * @date 2023/2/23 下午1:08
 */
public class MyResponse {
    private int status;

    private String openid;

    private byte[] icon_url;

    public MyResponse(byte[] icon_url) {
        this.icon_url = icon_url;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MyResponse() {
    }

    public MyResponse(int status, String token) {
        this.status = status;
        this.openid = token;
    }

    public MyResponse(int status) {
        this.status = status;
    }
}
