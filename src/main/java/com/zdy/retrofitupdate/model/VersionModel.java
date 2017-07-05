package com.zdy.retrofitupdate.model;


public class VersionModel {

    private static final long serialVersionUID = -6791012513790348762L;

    // 终端编号
    private String termCode;

    // 平台（i表示IOS / a表示安卓）
    private String plat;

    // 版本
    private String version;

    // 版本序号
    private Short versionNo;

    // apk下载地址
    private String apkUrl;

    // apk MD5
    private String apkMd5;

    // apk大小
    private Integer apkSize;

    // 更新日志
    private String log;

    // 是否强制更新（1强制更新）
    private Boolean isForce;

    // 是否更新（1更新）
    private Boolean isUpdate;

    public String getTermCode() {
        return termCode;
    }

    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Short getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Short versionNo) {
        this.versionNo = versionNo;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public Integer getApkSize() {
        return apkSize;
    }

    public void setApkSize(Integer apkSize) {
        this.apkSize = apkSize;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Boolean getIsForce() {
        return isForce;
    }

    public void setIsForce(Boolean force) {
        isForce = force;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean update) {
        isUpdate = update;
    }
}