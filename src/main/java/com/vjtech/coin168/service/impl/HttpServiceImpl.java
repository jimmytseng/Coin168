package com.vjtech.coin168.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vjtech.coin168.dao.DefCoinDao;
import com.vjtech.coin168.model.DefCoin;
import com.vjtech.coin168.service.HttpService;
import com.vjtech.coin168.utils.CommonUtils;

@Service
public class HttpServiceImpl implements HttpService {

    static final Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    @Value("${trust-store-path}")
    private String trustStorePath;
    
    @Autowired
    private DefCoinDao coinDao;

    @Transactional
    @Override
    public void getCoinFromCoinhunt() {

        try {

            // 獲取開始時間
            long startTime = System.currentTimeMillis();
            logger.info("===========start get coin job===============");

            if (StringUtils.isBlank(trustStorePath)) {
                URL res = getClass().getClassLoader().getResource("keystore.jks");
                File file = new File(res.toURI());
                trustStorePath = file.getAbsolutePath();
            }

            System.setProperty("javax.net.ssl.trustStore", trustStorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", "password");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");

            // 第一次打URL，取得cookie以及csrf token
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();

            HttpGet httpGet = new HttpGet("https://api.cnhnt.cc/public/getAllCoinsApproved?orderChoose=date&orderUp=false&from=10&size=100");
            HttpResponse response = httpClient.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity(), "utf-8");

            JsonObject jo = new Gson().fromJson(result, JsonObject.class);
            JsonArray ja = jo.getAsJsonArray("res");

            String buildId = "";

            for (int i = 0; i < ja.size(); i++) {
                JsonObject data = (JsonObject) ja.get(i);
                String coinCode = data.get("symbol").isJsonNull() ? null : data.get("symbol").getAsString();
                String coinId = data.get("id").isJsonNull() ? null : data.get("id").getAsString();

                if (i == 0) {
                    httpGet = new HttpGet("https://coinhunt.cc/coin/" + coinId);
                    response = httpClient.execute(httpGet);
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    buildId = StringUtils.substringBetween(result, ",\"buildId\":\"", "\",\"");
                    if (StringUtils.isBlank(buildId)) {
                        logger.error("===========================get coin fail=================================");
                        return;
                    }
                }

                // 代碼為空不存
                if (StringUtils.isBlank(coinCode)) {
                    continue;
                }
                // 代碼超過6碼不存
                if (StringUtils.length(coinCode) > 12) {
                    continue;
                }
                // 已經有的不存
                DefCoin tmp = coinDao.findByCoinCode(coinCode);
                if (null != tmp) {
                    continue;
                }
                try {
                    DefCoin coin = new DefCoin();
                    coin.setOid(CommonUtils.getUUIDString());
                    coin.setCoinCode(coinCode);

                    String coinName = data.get("name").isJsonNull() ? null : data.get("name").getAsString();
                    coin.setCoinName(coinName);

                    logger.info("coinCode:" + coinCode);
                    logger.info("coinName:" + coinName);

                    coin.setMarketCap(data.get("marketCap").isJsonNull() ? null : new BigDecimal(data.get("marketCap").getAsString()));
                    String imgUrl = data.get("logo").isJsonNull() ? null : data.get("logo").getAsString();
                    if (StringUtils.length(imgUrl) <= 200) {
                        coin.setImgUrl(imgUrl);
                    }
                    coin.setLaunchDate(data.get("launchDate").isJsonNull() ? null : new Timestamp(Long.parseLong(data.get("launchDate").getAsString())));
                    coin.setVotes(data.get("votesCount").isJsonNull() ? null : Integer.parseInt(data.get("votesCount").getAsString()));
                    String id = data.get("id").getAsString();
                    String dtlUrl = "https://coinhunt.cc/_next/data/" + buildId + "/coin/" + id + ".json";
                    httpGet = new HttpGet(dtlUrl);
                    response = httpClient.execute(httpGet);
                    result = EntityUtils.toString(response.getEntity(), "utf-8");
                    JsonObject dtl = new Gson().fromJson(result, JsonObject.class).get("pageProps").getAsJsonObject().get("coinData").getAsJsonObject();

                    // desc
                    coin.setCoinDesc(dtl.get("description").isJsonNull() ? null : dtl.get("description").getAsString());
                    // price
                    coin.setPrice(dtl.get("price").isJsonNull() ? BigDecimal.ZERO : new BigDecimal(dtl.get("price").getAsString()));

                    if (dtl.get("contracts").getAsJsonArray().size() > 0) {
                        String coinChain = dtl.get("contracts").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                        if (StringUtils.equalsAnyIgnoreCase("Binance Smart Chain", coinChain)) {
                            coin.setCoinChain("BSC");
                        }else {
                            logger.info("===============Coin Chain:" + coinChain);
                        }
                        coin.setCoinContract(dtl.get("contracts").getAsJsonArray().get(0).getAsJsonObject().get("value").getAsString());
                    }

                    JsonArray links = dtl.get("links").getAsJsonArray();
                    for (int j = 0; j < links.size(); j++) {
                        JsonObject link = links.get(j).getAsJsonObject();
                        String linUrl = link.get("value").isJsonNull() ? null : link.get("value").getAsString();
                        if (StringUtils.length(linUrl) > 200) {
                            continue;
                        }
                        if (StringUtils.equals("Telegram", link.get("name").getAsString())) {
                            coin.setTelegramUrl(linUrl);
                        }
                        if (StringUtils.equals("Website", link.get("name").getAsString())) {
                            coin.setWebsiteUrl(linUrl);
                        }
                        if (StringUtils.equals("Twitter", link.get("name").getAsString())) {
                            coin.setTwitterUrl(linUrl);
                        }
                    }
                    coin.setCreUser("SYSTEM");
                    coin.setCreDate(new Timestamp(System.currentTimeMillis()));
                    coinDao.save(coin);

                    Thread.sleep(1000);
                } catch (Exception e) {
                    // 單筆exception後，不要噴錯繼續執行下一筆
                    logger.error(e.getMessage(), e);
                }
            }
            // 獲取結束時間
            long endTime = System.currentTimeMillis();
            String msg = "spent " + (endTime - startTime) / 1000 + " seconds";
            logger.info(msg);
            logger.info("===========end get coin job===============");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (URISyntaxException e1) {
            logger.error(e1.getMessage(), e1);
        } finally {
            System.clearProperty("javax.net.ssl.trustStore");
            System.clearProperty("javax.net.ssl.trustStorePassword");
            System.clearProperty("javax.net.ssl.trustStoreType");

        }

    }

}
