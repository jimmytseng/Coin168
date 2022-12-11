package com.vjtech.coin168;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vjtech.coin168.dao.DefCoinDao;
import com.vjtech.coin168.model.CustVote;
import com.vjtech.coin168.model.DefCoin;
import com.vjtech.coin168.service.CoinService;
import com.vjtech.coin168.utils.CommonUtils;

@SpringBootTest
class Coin168ApplicationTests {

    @Autowired
    CoinService coinService;
    @Autowired
    DefCoinDao coinDao;
	@Test
	void contextLoads() {
	}

    @Test
    public void createCoin() {
        for (int i = 0; i < 200; i++) {
            DefCoin coin = new DefCoin();
            coin.setOid(CommonUtils.getUUIDString());
            coin.setCoinContract("0x" + CommonUtils.getUUIDString());
            coin.setCoinCode(getStringRandom(3));
            coin.setCoinName(coin.getCoinCode() + " COIN");
            coin.setMarketCap(getRandomBigDecimal("500000000"));
            coin.setPrice(getRandomBigDecimal("5000000"));
            coin.setVotes((int) (Math.random()*10 * i * 2000));
            coin.setVotesDaily((int) (Math.random()*10 * i * 1000));
            coin.setLaunchDate(new Timestamp(System.currentTimeMillis()));
            coin.setCreUser("SYSTEM");
            coinDao.save(coin);
        }
    }

//    @Test
//    public void createCustVote() {
//            CustVote v = new CustVote();
//            v.setOid(CommonUtils.getUUIDString());
//            v.setCoinCode("ETH");
//            v.setCustId("Vic");
//            v.setVoteDate(new Date());
//            coinService.saveCustVote(v);
//    }
//    
    
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            int temp = 65;
            val += (char)(random.nextInt(26) + temp);
        }
        return val;
    }
    
    public static BigDecimal getRandomBigDecimal(String range) {
        BigDecimal max = new BigDecimal(range + ".0");
        BigDecimal randFromDouble = new BigDecimal(Math.random());
        BigDecimal actualRandomDec = randFromDouble.divide(max,BigDecimal.ROUND_DOWN);
        return actualRandomDec;
    }
}
