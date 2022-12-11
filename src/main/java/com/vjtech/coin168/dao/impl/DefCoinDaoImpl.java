package com.vjtech.coin168.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dao.DefCoinDao;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.model.DefCoin;

@Repository("DefCoinDao")
public class DefCoinDaoImpl extends GenericDaoImpl<DefCoin> implements DefCoinDao {

    @Override
    public DefCoin findByCoinCode(String coinCode) {
        if (StringUtils.isBlank(coinCode)) {
            return null;
        }
        String sql = "SELECT * FROM DEF_COIN WHERE coinCode = :coinCode";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("coinCode", coinCode);
        return getByParams(sql, params);
    }

    @Override
    public PageData listByVotes(PageRequest pageRequest, Integer qryType, String custId) {
        StringBuffer sql = new StringBuffer();
        
        Map<String, Object> params = new HashMap<String, Object>();
        
        sql.append(" SELECT  CASE WHEN V.oid IS NULL THEN '0' ELSE '1' END AS voted, C.*  ");
        sql.append(" FROM DEF_COIN C  ");
        sql.append(" LEFT JOIN AD_COIN A ON C.coinCode = A.coinCode  ");
        sql.append("    AND CAST(GETDATE() AS DATE)  BETWEEN A.STARTDATE AND A.ENDDATE ");
        sql.append(" LEFT JOIN Cust_Vote V ON C.coinCode = V.coinCode  ");

        if (StringUtils.isBlank(custId)) {
            sql.append("    AND 1=2 ");
        }else {
            sql.append("    AND V.custId = :custId ");
            sql.append("    AND CONVERT(VARCHAR(10), V.voteDate, 102) = CONVERT(VARCHAR(10), GETDATE(), 102) ");
            params.put("custId", custId);
        }

        sql.append(" WHERE 1=1 AND A.OID IS NULL");
        if (StringUtils.isBlank(pageRequest.getQryCoin())) {
            sql.append(" AND VOTES > 500 ");
        }else {
            sql.append(" AND (C.coinCode like :qryCoin OR C.coinName like :qryCoin)");
            params.put("qryCoin", pageRequest.getQryCoin() + "%");
        }
        
        if (StringUtils.isNotBlank(pageRequest.getOrderColumn())) {

            sql.append(" ORDER BY  ");
            sql.append(pageRequest.getOrderColumn());
            sql.append(" ");
            sql.append(pageRequest.getOrderType());
            
        }else {
            if (qryType == 2) {
                // 今日
                sql.append(" ORDER BY VOTESDAILY DESC");
            } else {
                // 全部
                sql.append(" ORDER BY VOTES DESC");
            }
        }
        
        

        return findPageDataByParams(sql.toString(), params, pageRequest);
        
        
    }

    @Override
    public List<DefCoin> findPromoteCoin(String custId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);
        
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT CASE WHEN V.oid IS NULL THEN '0' ELSE '1' END AS voted, C.*  ");
        sql.append(" FROM AD_COIN A ");
        sql.append(" INNER JOIN DEF_COIN C ON A.COINCODE = C.COINCODE ");
        sql.append(" LEFT JOIN Cust_Vote V ON C.coinCode = V.coinCode  ");
        if (StringUtils.isBlank(custId)) {
            sql.append("    AND 1=2 ");
        }else {
            sql.append("    AND V.custId = :custId ");
            sql.append("    AND CONVERT(VARCHAR(10), V.voteDate, 102) = CONVERT(VARCHAR(10), GETDATE(), 102) ");
            params.put("custId", custId);
        }
        sql.append(" WHERE  CAST(GETDATE() AS DATE)  BETWEEN A.STARTDATE AND A.ENDDATE ");
        sql.append("    ORDER BY A.STARTDATE ");
        return getNamedJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<DefCoin>(DefCoin.class));
    }

    @Override
    public PageData listVotedCoin(PageRequest pageRequest, String custId) {

        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT DISTINCT C.*, '1' AS voted  from Cust_Vote V ");
        sql.append(" INNER JOIN DEF_COIN C ON V.coinCode = C.coinCode ");
        sql.append(" WHERE V.custId = :custId ");
        params.put("custId", custId);
        
        if (StringUtils.isNotBlank(pageRequest.getOrderColumn())) {
            sql.append(" ORDER BY  ");
            sql.append(pageRequest.getOrderColumn());
            sql.append(" ");
            sql.append(pageRequest.getOrderType());
            
        }else {

            sql.append(" ORDER BY VOTES DESC");
        }
        
        return findPageDataByParams(sql.toString(), params, pageRequest);
    }

    @Override
    public void resetVotesDaily() {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE DEF_COIN SET votesDaily = 0";
        getNamedJdbcTemplate().update(sql, params);
    }
}