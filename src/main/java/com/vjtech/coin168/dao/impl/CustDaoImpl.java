package com.vjtech.coin168.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.vjtech.coin168.dao.CustDao;
import com.vjtech.coin168.model.Cust;

@Repository("CustDao")
public class CustDaoImpl extends GenericDaoImpl<Cust> implements CustDao {

    @Override
    public Cust findByCustId(String custId) {
        String sql = "SELECT * FROM CUST WHERE custId = :custId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);
        return getByParams(sql, params);
    }
    
	@Override
    public Cust findByCustMail(String email) {
        String sql = "SELECT * FROM CUST WHERE email = :email and provider = 'local'";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);
        return getByParams(sql, params);
    }
}