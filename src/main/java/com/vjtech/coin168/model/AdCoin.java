package com.vjtech.coin168.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AD_COIN")
public class AdCoin {

    @Id
	@Column(name = "oid", columnDefinition = "char(32)", length = 32)
	private String oid;

	@Column(name = "coinCode", columnDefinition = "varchar(12)", length = 12)
	private String coinCode;

	@Column(name = "startDate", columnDefinition = "datetime")
	private Date startDate;

	@Column(name = "endDate", columnDefinition = "datetime")
	private Date endDate;

	@Column(name = "creUser", columnDefinition = "varchar(32)", length = 32)
	private String creUser;

	@Column(name = "creDate", columnDefinition = "datetime")
	private Timestamp creDate;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCoinCode() {
		return coinCode;
	}

	public void setCoinCode(String coinCode) {
		this.coinCode = coinCode;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
	
	

}