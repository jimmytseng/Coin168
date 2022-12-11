package com.vjtech.coin168.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.vjtech.coin168.service.CoinService;
import com.vjtech.coin168.service.HttpService;

@Controller
public class BatchController {

    @Autowired
    private HttpService httpService;
    @Autowired
    private CoinService coinService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void getCoinFromCoinhunt() {
        httpService.getCoinFromCoinhunt();
    }
    
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetVotesDaily() {
        coinService.resetVotesDaily();
    }
}
