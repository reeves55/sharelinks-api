package com.xiaolee.sharelinksApi.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_comments")
@DynamicUpdate
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;
    @Column(name = "fuser_name")
    private String fuserName;
    @Column(name = "fpost_id")
    private Long fpostId;
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

    public String getFuserName() {
        return fuserName;
    }

    public void setFuserName(String fuserName) {
        this.fuserName = fuserName;
    }

    public Long getFpostId() {
        return fpostId;
    }

    public void setFpostId(Long fpostId) {
        this.fpostId = fpostId;
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
