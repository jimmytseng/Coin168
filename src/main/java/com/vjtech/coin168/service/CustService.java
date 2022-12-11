package com.vjtech.coin168.service;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.model.Cust;

public interface CustService {
	
	 public BaseResult registerCust(Cust cust,String lang);
	 
	 public Cust getCustById(String custId);

     public void sendMail(String custId, String lang);

     public void verifyCust(String custId, String verifyCode);
     
	 public void save(Cust cust);

	 public void saveContact(String custName, String email, String subject, String message);

	void registerGoogleCust(Cust cust);
}
