package com.vjtech.coin168.dao;

import java.util.List;

import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.model.DefCoin;

public interface DefCoinDao extends GenericDao<DefCoin> {

	DefCoin findByCoinCode(String coinCode);
	
	public PageData listByVotes(PageRequest pageRequest, Integer qryType, String custId);

    List<DefCoin> findPromoteCoin(String custId);
    
    public PageData listVotedCoin(PageRequest pageRequest, String custId);
    
    public void resetVotesDaily();
}