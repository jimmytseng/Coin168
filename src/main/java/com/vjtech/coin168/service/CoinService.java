package com.vjtech.coin168.service;

import java.util.Date;
import java.util.List;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.model.Cust;
import com.vjtech.coin168.model.CustVote;
import com.vjtech.coin168.model.DefCoin;

public interface CoinService {

    public List<DefCoin> findAllCoin();
    
    public List<Cust> findAllCust();
    
    public BaseResult saveCoin(DefCoin coin);

    public List<CustVote> findCustVote(String custId);
    
    public DefCoin fidnDefCoin(String oid);
    
    public BaseResult addVote(String coinCode, String custId);

	public PageData listByVotes(PageRequest pageRequest, Integer qryType);

    public PageData listVotedCoin(PageRequest pageRequest);
    
    public BaseResult verifyCoinCode(String coinCode);

    public BaseResult promoteCoin(String coinCode, Date startDate, Date endDate, Integer addVotes);
    
    public List<DefCoin> listPprmoteCoin();
    
    public BaseResult delCoin(String oid);
    
    public void resetVotesDaily();
}
