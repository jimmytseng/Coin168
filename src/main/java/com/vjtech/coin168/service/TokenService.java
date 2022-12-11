package com.vjtech.coin168.service;

public interface TokenService {

	public String getAccessToken(String username,String password);
	
	public void revokeToken(String accessToken);

	public String getAccessClientCredentialToken();
}
