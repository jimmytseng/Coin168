package com.vjtech.coin168.dao;

import java.util.Date;
import java.util.List;

import com.vjtech.coin168.model.CustVote;

public interface CustVoteDao extends GenericDao<CustVote> {
    
    public List<CustVote> findTodayVotes(String custId);

	public int findDailVotesCount(String custId);
	
}