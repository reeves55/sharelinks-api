package com.xiaolee.sharelinksApi.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_users")
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;
    @Column(name = "fname")
    private String fname;
    @Column
    private int ftype;
    @Column
    private String fpassword;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getFtype() {
        return ftype;
    }

    public void setFtype(int ftype) {
        this.ftype = ftype;
    }

    public String getFpassword() {
        return fpassword;
    }

    public void setFpassword(String fpassword) {
        this.fpassword = fpassword;
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
