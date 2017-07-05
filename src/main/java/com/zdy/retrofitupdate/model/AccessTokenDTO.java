package com.zdy.retrofitupdate.model;

import java.util.Date;

/**
 * Created by hanley on 2016/6/23.
 */
public class AccessTokenDTO {


    // AccessToken
    private String accessTok;

    // 设备终端类型
    private String deviceType;

    // 创建时间
    private Date createAt;

    // 过期时间
    private int expireIn;

    // 成员id
    private Integer memberId;

    // 终端设备品牌
    private String deviceBrand;

    // 终端设备型号
    private String deviceModel;

    // 设备唯一标识
    private String imei;

    public String getAccessTok() {
        return accessTok;
    }

    public void setAccessTok(String accessTok) {
        this.accessTok = accessTok;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
