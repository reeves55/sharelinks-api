package com.xiaolee.sharelinksApi.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_notifications")
@DynamicUpdate
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;
    @Column
    private String ftype;
    @Column
    private String forigin;
    @Column
    private String fto;
    @Column
    private String fcontent;
    @Column(name = "fadded_time")
    private Date faddedTime;
    @Column(name = "fupdate_time")
    private Date fupdateTime;
    @Column
    private String fenable;

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getForigin() {
        return forigin;
    }

    public void setForigin(String forigin) {
        this.forigin = forigin;
    }

    public String getFto() {
        return fto;
    }

    public void setFto(String fto) {
        this.fto = fto;
    }

    public String getFcontent() {
        return fcontent;
    }

    public void setFcontent(String fcontent) {
        this.fcontent = fcontent;
    }

    public Date getFaddedTime() {
        return faddedTime;
    }

    public void setFaddedTime(Date faddedTime) {
        this.faddedTime = faddedTime;
    }

    public Date getFupdateTime() {
        return fupdateTime;
    }

    public void setFupdateTime(Date fupdateTime) {
        this.fupdateTime = fupdateTime;
    }

    public String getFenable() {
        return fenable;
    }

    public void setFenable(String fenable) {
        this.fenable = fenable;
    }
}
