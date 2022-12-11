package com.vjtech.coin168.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUST")
public class Cust implements DataObject {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "oid", columnDefinition = "char(32)", length = 32)
    private String oid;
    
    @Column(name = "custId", columnDefinition = "varchar(12)", length = 12)
    private String custId;

    @Column(name = "custName", columnDefinition = "varchar(64)", length = 64)
    private String custName;

    @Column(name = "birthday", columnDefinition = "date")
    private Date birthday;

    @Column(name = "sex", columnDefinition = "char(1)", length = 1)
    private String sex;

    @Column(name = "email", columnDefinition = "varchar(64)", length = 64)
    private String email;

    @Column(name = "pwd", columnDefinition = "char(128)", length = 128)
    private String pwd;
    
    @Column(name = "authority", columnDefinition = "varchar(10)", length = 10)
    private String authority;

    @Column(name = "creDate", columnDefinition = "datetime")
    private Timestamp creDate;
    
    @Column(name = "status", columnDefinition = "bit")
    private Boolean status;
    
    @Column(name = "provider", columnDefinition = "varchar(12)",length = 12)
    private String provider;


	public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}
}