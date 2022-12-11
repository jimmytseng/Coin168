package com.vjtech.coin168.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUST_VOTE")
public class CustVote implements DataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid", columnDefinition = "char(32)", length = 32)
    private String oid;

    @Column(name = "voteDate", columnDefinition = "date")
    private Date voteDate;

    @Column(name = "custId", columnDefinition = "varchar(12)", length = 12)
    private String custId;

    @Column(name = "coinCode", columnDefinition = "varchar(12)", length = 12)
    private String coinCode;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(Date voteDate) {
        this.voteDate = voteDate;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }
}