package com.vjtech.coin168.dao;

import com.vjtech.coin168.model.CustVerify;

public interface CustVerifyDao extends GenericDao<CustVerify> {
    
    CustVerify findByCustId(String custId);
}