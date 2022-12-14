package com.vjtech.coin168.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.CommonEnum;
import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dao.AdcoinDao;
import com.vjtech.coin168.dao.CustDao;
import com.vjtech.coin168.dao.CustVoteDao;
import com.vjtech.coin168.dao.DefCoinDao;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.exception.InfoException;
import com.vjtech.coin168.model.AdCoin;
import com.vjtech.coin168.model.Cust;
import com.vjtech.coin168.model.CustVote;
import com.vjtech.coin168.model.DefCoin;
import com.vjtech.coin168.service.CoinService;
import com.vjtech.coin168.utils.CommonUtils;

@Service
public class CoinServiceImpl implements CoinService {

    static final Logger logger = LoggerFactory.getLogger(CoinServiceImpl.class);

    @Autowired
    private DefCoinDao defCoinDao;
    @Autowired
    private CustDao custDao;
    @Autowired
    private CustVoteDao custVoteDao;
    @Autowired
    private AdcoinDao adcoinDao;

    @Override
    public List<DefCoin> findAllCoin() {
        return defCoinDao.findAll();
    }

    @Override
    public List<Cust> findAllCust() {
        return custDao.findAll();
    }

    @Override
    public List<CustVote> findCustVote(String custId) {
        return custVoteDao.findTodayVotes(custId);
    }

    @Override
    @Transactional
    public BaseResult saveCoin(DefCoin coin) {
        DefCoin existCoin = defCoinDao.findByCoinCode(coin.getCoinCode());
        if (null != existCoin) {
            throw new InfoException("Coin code is already existed.");
        }
        DefCoin newCoin = new DefCoin();
        BeanUtils.copyProperties(coin, newCoin);

        if (StringUtils.isNotBlank(newCoin.getImgUrl()) && !StringUtils.startsWith(newCoin.getImgUrl(), "http")) {
            newCoin.setImgUrl("https://" + newCoin.getImgUrl());
        } else {
            newCoin.setImgUrl(newCoin.getImgUrl());
        }

        if (StringUtils.isNotBlank(newCoin.getTwitterUrl()) && !StringUtils.startsWith(newCoin.getTwitterUrl(), "http")) {
            newCoin.setTwitterUrl("https://" + newCoin.getTwitterUrl());
        } else {
            newCoin.setTwitterUrl(newCoin.getTwitterUrl());
        }

        if (StringUtils.isNotBlank(newCoin.getTelegramUrl()) && !StringUtils.startsWith(newCoin.getTelegramUrl(), "http")) {
            newCoin.setTelegramUrl("https://" + newCoin.getTelegramUrl());
        } else {
            newCoin.setTelegramUrl(newCoin.getTelegramUrl());
        }

        if (StringUtils.isNotBlank(newCoin.getWebsiteUrl()) && !StringUtils.startsWith(newCoin.getWebsiteUrl(), "http")) {
            newCoin.setWebsiteUrl("https://" + newCoin.getWebsiteUrl());
        } else {
            newCoin.setWebsiteUrl(newCoin.getWebsiteUrl());
        }

        newCoin.setOid(CommonUtils.getUUIDString());
        newCoin.setCreUser(SecurityContextHolder.getContext().getAuthentication().getName());
        newCoin.setCreDate(new Timestamp(System.currentTimeMillis()));
        newCoin.setVotes(0);
        newCoin.setVotesDaily(0);
        defCoinDao.save(newCoin);
        return new BaseResult(CommonEnum.SUCCESS, "??????????????????");
    }

    @Override
    public DefCoin fidnDefCoin(String oid) {
        return custVoteDao.findById(DefCoin.class, oid);
    }

    @Override
    @Transactional
    public BaseResult addVote(String coinCode, String custId) {

        // ???custId????????????????????????
        List<CustVote> list = custVoteDao.findTodayVotes(custId);

        // ??????????????????
        boolean isCancel = false;
        CustVote cancelVote = null;

        if (CollectionUtils.isNotEmpty(list)) {
            for (CustVote custVote : list) {
                if (StringUtils.equals(custVote.getCoinCode(), coinCode)) {
                    isCancel = true;
                    cancelVote = custVote;
                    break;
                }
            }

            // ??????????????????????????????????????????
            if (!isCancel && list.size() >= 5) {
                throw new InfoException("??????????????????????????????????????????");
            }
        }

        DefCoin defCoin = defCoinDao.findByCoinCode(coinCode);

        BaseResult result = null;

        if (isCancel) {
            // ????????????
            custVoteDao.delete(cancelVote);
            defCoin.setVotes(null == defCoin.getVotes() ? 0 : defCoin.getVotes() - 1);
            defCoin.setVotesDaily(null == defCoin.getVotesDaily() ? 0 : defCoin.getVotesDaily() - 1);
            result = new BaseResult(CommonEnum.CANCEL_VOTE_SUCCESS, "??????????????????");
        } else {
            // ????????????
            CustVote custVote = new CustVote();
            custVote.setCustId(custId);
            custVote.setVoteDate(new Date());
            custVote.setCoinCode(coinCode);
            custVote.setOid(CommonUtils.getUUIDString());
            custVoteDao.save(custVote);
            defCoin.setVotes(null == defCoin.getVotes() ? 0 : defCoin.getVotes() + 1);
            defCoin.setVotesDaily(null == defCoin.getVotesDaily() ? 0 : defCoin.getVotesDaily() + 1);
            result = new BaseResult(CommonEnum.SUCCESS, "????????????");
        }
        defCoinDao.save(defCoin);

        return result;
    }

    @Override
    public PageData listByVotes(PageRequest pageRequest, Integer qryType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String custId = authentication.getName();
        if (StringUtils.equals(custId, "anonymousUser")) {
            custId = null;
        }
        return defCoinDao.listByVotes(pageRequest, qryType, custId);
    }

    @Override
    public BaseResult verifyCoinCode(String coinCode) {
        if (StringUtils.isBlank(coinCode)) {
            return null;
        }
        DefCoin coin = defCoinDao.findByCoinCode(coinCode);
        BaseResult result = null;
        if (null == coin) {
            result = new BaseResult(CommonEnum.SUCCESS);
        } else {
            result = new BaseResult(CommonEnum.ALREADY_EXISTED);
        }
        return result;
    }

    @Override
    @Transactional
    public BaseResult promoteCoin(String coinCode, Date startDate, Date endDate, Integer addVotes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) authentication.getPrincipal();
        String custId = userDetail.getUsername();
        // if (!StringUtils.equals("asd", userDetail.getUsername())) {
        // return new BaseResult(CommonEnum.PERMISSION_DENIED);
        // }

        DefCoin coin = defCoinDao.findByCoinCode(coinCode);
        if (null == coin) {
            throw new InfoException("??????????????????");
        }

        AdCoin ad = new AdCoin();
        ad.setCoinCode(coinCode);
        ad.setStartDate(startDate);
        ad.setEndDate(endDate);
        ad.setCreUser(custId);
        ad.setCreDate(new Timestamp(System.currentTimeMillis()));
        ad.setOid(CommonUtils.getUUIDString());
        adcoinDao.save(ad);

        // ????????????
        if (null != addVotes) {
            coin.setVotes(coin.getVotes() + addVotes);
            coin.setVotesDaily(null == coin.getVotesDaily() ? 0 : coin.getVotesDaily() + addVotes);
            defCoinDao.save(coin);
        }

        return new BaseResult(CommonEnum.SUCCESS, "??????????????????");
    }

    @Override
    public List<DefCoin> listPprmoteCoin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String custId = authentication.getName();
        if (StringUtils.equals(custId, "anonymousUser")) {
            custId = null;
        }
        return defCoinDao.findPromoteCoin(custId);
    }

    @Override
    public BaseResult delCoin(String oid) {
        if (StringUtils.isBlank(oid)) {
            return new BaseResult(CommonEnum.PARAM_NOT_NULL);
        }

        DefCoin coin = defCoinDao.find(oid);
        if (null == coin) {
            return new BaseResult(CommonEnum.FAILED);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            defCoinDao.delete(coin);
        } else {
            if (StringUtils.equals(SecurityContextHolder.getContext().getAuthentication().getName(), coin.getCreUser())) {
                defCoinDao.delete(coin);
            } else {
                return new BaseResult(CommonEnum.PERMISSION_DENIED);
            }
        }

        return new BaseResult(CommonEnum.SUCCESS);
    }

    @Override
    public PageData listVotedCoin(PageRequest pageRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String custId = authentication.getName();
        if (StringUtils.equals(custId, "anonymousUser")) {
            custId = null;
            throw new InfoException("??????????????????");
        }
        return defCoinDao.listVotedCoin(pageRequest, custId);
    }

    @Override
    @Transactional
    public void resetVotesDaily() {
        defCoinDao.resetVotesDaily();
    }

}
