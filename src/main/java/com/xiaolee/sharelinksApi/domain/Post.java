package com.xiaolee.sharelinksApi.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_posts")
@DynamicUpdate
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fid")
    private Long fid;
    @Column
    private String ftitle;
    @Column
    private String furl;
    @Column(name = "fadded_time")
    private Date faddedTime;
    @Column(name = "fupdate_time")
    private Date fupdateTime;
    @Column
    private String fauthor;
    @Column
    private String fcategory;
    @Column
    private int flike;
    @Column
    private String fenable;
    @Column
    private int freported;
    @Column
    private String fshare;
    @Column
    private String ftag;

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getFtitle() {
        return ftitle;
    }

    public void setFtitle(String ftitle) {
        this.ftitle = ftitle;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
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

    public String getFauthor() {
        return fauthor;
    }

    public void setFauthor(String fauthor) {
        this.fauthor = fauthor;
    }

    public String getFcategory() {
        return fcategory;
    }

    public void setFcategory(String fcategory) {
        this.fcategory = fcategory;
    }

    public int getFlike() {
        return flike;
    }

    public void setFlike(int flike) {
        this.flike = flike;
    }

    public String getFenable() {
        return fenable;
    }

    public void setFenable(String fenable) {
        this.fenable = fenable;
    }

    public int getFreported() {
        return freported;
    }

    public void setFreported(int freported) {
        this.freported = freported;
    }

    public String getFshare() {
        return fshare;
    }

    public void setFshare(String fshare) {
        this.fshare = fshare;
    }

    public String getFtag() {
        return ftag;
    }

    public void setFtag(String ftag) {
        this.ftag = ftag;
    }
}
