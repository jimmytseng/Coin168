package com.vjtech.coin168.dao;

import com.vjtech.coin168.model.Cust;

public interface CustDao extends GenericDao<Cust> {
    
    Cust findByCustId(String custId);

	Cust findByCustMail(String email);
}