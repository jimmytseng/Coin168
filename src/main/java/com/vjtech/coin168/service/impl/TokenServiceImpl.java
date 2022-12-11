package com.vjtech.coin168.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.vjtech.coin168.service.TokenService;

import io.netty.util.internal.StringUtil;

@Service
public class TokenServiceImpl implements TokenService {

	public static Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

	@Override
	public String getAccessToken(String username, String password) {
		RestTemplate restTemplate = new RestTemplate();
		// header
		HttpHeaders headers = getHeader();
		MultiValueMap<String, String> variable = getVariable(username, password);
		HttpEntity<?> entity = new HttpEntity<Object>(variable, headers);
		String body = StringUtil.EMPTY_STRING;
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.postForEntity("http://localhost:8090/oauth/token", entity, String.class);
			body = responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			body = e.getResponseBodyAsString();
		}

		String accessToken = comsumeBody(body);
		return accessToken;
	}

	private String comsumeBody(String body) {
		// HTTP OK
		JSONObject jsonObject = new JSONObject(body);
		if (jsonObject.has(OAuth2Exception.ERROR)) {
			switch (jsonObject.getString(OAuth2Exception.ERROR)) {
			case OAuth2Exception.INVALID_GRANT:
				logger.error("invalid_grant");
				if (StringUtils.equals("Bad credentials", jsonObject.getString("error_description")))
					throw new InvalidGrantException("登入失败");
				// case OAuth2Exception.INVALID_TOKEN:
				// logger.error("invalid_token");
				// throw new InvalidTokenException("token 已过期");
			default:
				logger.error("Unexpect Error: " + jsonObject.getString("error"));
				throw new OAuth2Exception("OAuth 错误");
			}
		}
		return jsonObject.getString("token_type") + " " + jsonObject.getString("access_token");
	}

	private HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic dmp0ZWNrOnNlY3JldA==");
		// body
		return headers;
	}

	private MultiValueMap<String, String> getVariable(String username, String password) {
		MultiValueMap<String, String> variable = new LinkedMultiValueMap<>();
		variable.set("username", username);
		variable.set("password", password);
		variable.set("grant_type", "password");
		return variable;
	}

	public String getAccessClientCredentialToken() {
		RestTemplate restTemplate = new RestTemplate();
		// heade
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic ZGFzOnNlY3JldA==");
		// body
		MultiValueMap<String, String> variable = new LinkedMultiValueMap<>();
		variable.set("grant_type", "client_credentials");
		
		HttpEntity<?> entity = new HttpEntity<Object>(variable, headers);
		String body = StringUtil.EMPTY_STRING;
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.postForEntity("127.0.0.1:8090" + "/oauth/token", entity, String.class);
			body = responseEntity.getBody();
		} catch (HttpClientErrorException e) {
			body = e.getResponseBodyAsString();
		}

		String accessToken = comsumeBody(body);
		return accessToken;
	}

	@Override
	public void revokeToken(@RequestBody String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        // header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // body
        Map<String, String> variable = new HashMap<>();
        variable.put("accessToken", accessToken);
        HttpEntity<?> entity = new HttpEntity<Object>(variable, headers);
        try {
            restTemplate.postForEntity( "127.0.0.1:8090" + "/revoke/token", entity,Void.class);
        }catch(HttpClientErrorException e) {
            logger.error(e.getMessage());
        }
	}

}
