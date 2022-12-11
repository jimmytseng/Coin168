package com.vjtech.coin168.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.CommonEnum;
import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.model.AdCoin;
import com.vjtech.coin168.model.DefCoin;
import com.vjtech.coin168.service.CoinService;
import com.vjtech.coin168.service.HttpService;
import com.vjtech.coin168.utils.CommonUtils;

@RestController
public class CoinController {

    @Autowired
    CoinService coinService;
    @Autowired
    HttpService httpService;

    @GetMapping("getCoin")
    public BaseResult getCoin() {
        httpService.getCoinFromCoinhunt();
        return new BaseResult(CommonEnum.SUCCESS);
    }

    /**
     * list coin all (no auth)
     * 
     * @param pageRequest
     * @param queryType
     * @return
     */
    @GetMapping("listCoinAll")
    public PageData listCoinAll(PageRequest pageRequest) {
        PageData pageData = coinService.listByVotes(pageRequest, 1);
        return pageData;
    }

    /**
     * list coin daily (no auth)
     * 
     * @param pageRequest
     * @param queryType
     * @return
     */
    @GetMapping("listCoinDaily")
    public PageData listCoinDaily(PageRequest pageRequest) {
        PageData pageData = coinService.listByVotes(pageRequest, 2);
        return pageData;
    }

    /**
     * list coin all (no auth)
     * 
     * @param pageRequest
     * @param queryType
     * @return
     */
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
    @GetMapping("listVoted")
    public PageData listVoted(PageRequest pageRequest) {
        PageData pageData = coinService.listVotedCoin(pageRequest);
        return pageData;
    }
    
    /**
     * list promote coin (no auth)
     * 
     * @param pageRequest
     * @param queryType
     * @return
     */
    @GetMapping("listPormoteCoin")
    public BaseResult listPormoteCoin() {
        List<DefCoin> list = coinService.listPprmoteCoin();
        BaseResult result = new BaseResult(CommonEnum.SUCCESS, list);
        return result;
    }

    
    /**
     * list coin content (no auth)
     * 
     * @param oid
     * @return
     */
    @GetMapping("coinDetail")
    public BaseResult listCoinContent(@RequestParam("oid") String oid) {
        DefCoin refCoin = coinService.fidnDefCoin(oid);
        return new BaseResult(CommonEnum.SUCCESS, refCoin);
    }

    /**
     * add coin (user auth)
     * 
     * @param defCoin
     * @return
     */
    @PostMapping("addCoin")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
    public BaseResult addCoin(DefCoin defCoin) {
        BaseResult result = null;
         result = coinService.saveCoin(defCoin);
        return result;
    }

    /**
     * addAdCoin (admin auth)
     * 
     * @param defCoin
     * @return
     */
    @PostMapping("addAdCoin")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public BaseResult addAdCoin(AdCoin adCoin, String startDate, String endDate) {
        // wait valid
        adCoin.setOid(CommonUtils.getUUIDString());
        adCoin.setCreDate(new Timestamp(System.currentTimeMillis()));
        adCoin.setStartDate(null);
        adCoin.setEndDate(null);
        // wait
        adCoin.setCreUser("");
        return new BaseResult(CommonEnum.SUCCESS);
    }

    /**
     * (user auth) add vote
     * 
     * @param coinCode
     * @param custId
     * @return BaseResult
     */
    @PostMapping("addVote")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
    public BaseResult addVote(String coinCode, String custId) {
        BaseResult result = null;
        result = coinService.addVote(coinCode, custId);
        return result;
    }

    /**
     * verify coincode
     * 
     * @param coinCode
     * @return BaseResult
     */
    @GetMapping("verifyCoinCode")
    public BaseResult verifyCoinCode(String coinCode) {
        if (StringUtils.isBlank(coinCode)) {
            return new BaseResult(CommonEnum.PARAM_NOT_NULL);
        }
        BaseResult result = coinService.verifyCoinCode(coinCode);
        return result;
    }

    /**
     * (admin auth) promoteCoin
     * 
     * @param coinCode
     * @param custId
     * @return BaseResult
     */
    @PostMapping("promoteCoin")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public BaseResult promoteCoin(String coinCode, Date startDate, Date endDate, Integer addVotes) {
        BaseResult result = null;
        result = coinService.promoteCoin(coinCode, startDate, endDate, addVotes);
        return result;
    }

    /**
     * (user auth) del Coin
     * 
     * @param coin oid
     * @return BaseResult
     */
    @GetMapping("delCoin")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')") 
    public BaseResult delCoin(String oid) {
        BaseResult result = null;
        result = coinService.delCoin(oid);
        return result;
    }
}
