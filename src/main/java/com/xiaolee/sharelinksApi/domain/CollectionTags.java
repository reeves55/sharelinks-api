package com.xiaolee.sharelinksApi.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_usercoltags")
@DynamicUpdate
public class CollectionTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;
    @Column
    private String fuser;
    @Column
    private String ftag;
    @Column(name = "fpost_id")
    private Long fpostId;
    @Column
    private String fshare;
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

    public String getFuser() {
        return fuser;
    }

    public void setFuser(String fuser) {
        this.fuser = fuser;
    }

    public String getFtag() {
        return ftag;
    }

    public void setFtag(String ftag) {
        this.ftag = ftag;
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

    public String getFshare() {
        return fshare;
    }

    public void setFshare(String fshare) {
        this.fshare = fshare;
    }

    public void setFenable(String fenable) {
        this.fenable = fenable;
    }

    public Long getFpostId() {
        return fpostId;
    }

    public void setFpostId(Long fpostId) {
        this.fpostId = fpostId;
    }
}
