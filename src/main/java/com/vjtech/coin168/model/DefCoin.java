package com.vjtech.coin168.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "DEF_COIN")
public class DefCoin implements DataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid", columnDefinition = "char(32)", length = 32, nullable = false)
    private String oid;

    @Column(name = "coinChain", columnDefinition = "varchar(12)", length = 12)
    private String coinChain;

    @Column(name = "coinContract", columnDefinition = "varchar(64)", length = 64)
    private String coinContract;

    @Column(name = "coinCode", columnDefinition = "varchar(12)", length = 12, nullable = false)
    private String coinCode;

    @Column(name = "coinName", columnDefinition = "varchar(64)", length = 64, nullable = false)
    private String coinName;

    @Column(name = "coinDesc", columnDefinition = "varchar(2000)", length = 2000)
    private String coinDesc;

    @Column(name = "price", columnDefinition = "money")
    private BigDecimal price;

    @Column(name = "marketCap", columnDefinition = "money")
    private BigDecimal marketCap;

    @Column(name = "launchDate", columnDefinition = "date")
    private Timestamp launchDate;

    @Column(name = "imgUrl", columnDefinition = "varchar(200)", length = 200)
    private String imgUrl;
    
    @Column(name = "telegramUrl", columnDefinition = "varchar(200)", length = 200)
    private String telegramUrl;

    @Column(name = "twitterUrl", columnDefinition = "varchar(200)", length = 200)
    private String twitterUrl;

    @Column(name = "websiteUrl", columnDefinition = "varchar(200)", length = 200)
    private String websiteUrl;

    @Column(name = "votes", columnDefinition = "int")
    private Integer votes;

    @Column(name = "votesDaily", columnDefinition = "int")
    private Integer votesDaily;

    @Column(name = "creUser", columnDefinition = "varchar(32)", length = 32)
    private String creUser;

    @Column(name = "creDate", columnDefinition = "datetime")
    private Timestamp creDate;

    @Transient
    private String voted;

    public String getCoinContract() {
        return coinContract;
    }

    public void setCoinContract(String coinContract) {
        this.coinContract = coinContract;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinDesc() {
        return coinDesc;
    }

    public void setCoinDesc(String coinDesc) {
        this.coinDesc = coinDesc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getVotesDaily() {
        return votesDaily;
    }

    public void setVotesDaily(Integer votesDaily) {
        this.votesDaily = votesDaily;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public Timestamp getCreDate() {
        return creDate;
    }

    public void setCreDate(Timestamp creDate) {
        this.creDate = creDate;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Timestamp getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Timestamp launchDate) {
        this.launchDate = launchDate;
    }

    public String getCoinChain() {
        return coinChain;
    }

    public void setCoinChain(String coinChain) {
        this.coinChain = coinChain;
    }
}