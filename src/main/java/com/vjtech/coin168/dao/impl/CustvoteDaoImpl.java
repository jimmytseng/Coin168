package com.vjtech.coin168.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dao.CustVoteDao;
import com.vjtech.coin168.model.CustVote;
import com.vjtech.coin168.utils.DateUtils;

@Repository("CustvoteDao")
public class CustvoteDaoImpl extends GenericDaoImpl<CustVote> implements CustVoteDao {

	@Override
	public List<CustVote> findTodayVotes(String custId) {
		if (StringUtils.isBlank(custId)) {
			return null;
		}
		String sql = "SELECT * FROM CUST_VOTE WHERE custId = :custId AND CONVERT(VARCHAR(10), voteDate, 102) = CONVERT(VARCHAR(10), GETDATE(), 102)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("custId", custId);
		return findByParams(sql, params);
	}

	@Override
	public int findDailVotesCount(String custId) {
		String today = DateUtils.formatDate(new Date(), DateUtils.YYYYMMdd_SPLASH);
		
		String sql = "SELECT count(*) FROM Cust_Vote where voteDate = :today and custId =:custId";
		
		Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("today", today);
		query.setParameter("custId", custId);
		int count = ((Number) query.getSingleResult()).intValue();
		return count;
	}

}