package com.vjtech.coin168.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.vjtech.coin168.dao.CustVerifyDao;
import com.vjtech.coin168.model.CustVerify;

@Repository("CustVerifyDao")
public class CustVerifyDaoImpl extends GenericDaoImpl<CustVerify> implements CustVerifyDao {

    @Override
    public CustVerify findByCustId(String custId) {
        if (StringUtils.isBlank(custId)) {
            return null;
        }
        String sql = "SELECT * FROM CUST_VERIFY WHERE custId = :custId ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);
        return getByParams(sql, params);
    }

}