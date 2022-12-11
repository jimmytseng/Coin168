package com.vjtech.coin168.dao.impl;
import org.springframework.stereotype.Repository;

import com.vjtech.coin168.dao.AdcoinDao;
import com.vjtech.coin168.model.AdCoin;


@Repository("AdcoinDao")
public class AdcoinDaoImpl extends GenericDaoImpl<AdCoin> implements AdcoinDao{
}