package com.vjtech.coin168.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUST_VERIFY")
public class CustVerify implements DataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid", columnDefinition = "char(32)", length = 32)
    private String oid;

    @Column(name = "custId", columnDefinition = "varchar(12)", length = 12)
    private String custId;

    @Column(name = "verifyCode", columnDefinition = "char(6)", length = 6)
    private String verifyCode;
    
    @Column(name = "creDate", columnDefinition = "datetime")
    private Timestamp creDate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Timestamp getCreDate() {
        return creDate;
    }

    public void setCreDate(Timestamp creDate) {
        this.creDate = creDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}