package com.vjtech.coin168.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONTACT")
public class Contact implements DataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid", columnDefinition = "char(32)", length = 32)
    private String oid;
    
    @Column(name = "custName", columnDefinition = "varchar(64)", length = 64)
    private String custName;

    @Column(name = "email", columnDefinition = "varchar(64)", length = 64)
    private String email;

    @Column(name = "subject", columnDefinition = "nvarchar(128)", length = 128)
    private String subject;
    
    @Column(name = "message", columnDefinition = "varchar(2000)", length = 2000)
    private String message;

    @Column(name = "creDate", columnDefinition = "datetime")
    private Timestamp creDate;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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