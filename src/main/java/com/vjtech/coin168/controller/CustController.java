package com.vjtech.coin168.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.CommonEnum;
import com.vjtech.coin168.model.Cust;
import com.vjtech.coin168.service.CustService;
import com.vjtech.coin168.service.TokenService;
import com.vjtech.coin168.service.impl.MailService;
import com.vjtech.coin168.utils.CommonUtils;

import io.netty.util.internal.StringUtil;

@RestController
public class CustController {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private CustService custServcie;

    @Autowired
    private MailService mailService;
    
    private final String Redirect_Uri = "https://coin168.cc/LoginGoogle";

	@PostMapping("login")
	public BaseResult login(String custId, String password, String lang, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String token = tokenService.getAccessToken(custId, password);
		Map<String, Object> map = new HashMap<>();
		map.put("access_token", token);
		
		Cust cust = custServcie.getCustById(custId);
		if (null != cust) {
		    // 帳號未啟用不給登入
		    if (!cust.getStatus()) {
		        custServcie.sendMail(custId,lang);
		        return new BaseResult(CommonEnum.ACCOUNT_NOT_ENABLE);
            }
		    map.put("role", cust.getAuthority());
		    map.put("name", cust.getCustName());
        }
		
		return new BaseResult(CommonEnum.SUCCESS.getCode(), "success", map);
	}

	@PostMapping("register")
	public BaseResult register(Cust cust, String lang) {
		BaseResult result = null;
		result = custServcie.registerCust(cust,lang);
		return result;
	}
	
	
	@GetMapping("code")
	public BaseResult googleCallback(@RequestParam String code) {
		
		RestTemplate restTemplate = new RestTemplate();
		// header
		HttpHeaders headers =new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> variable = new LinkedMultiValueMap<>();
		variable.set("code", code);
		variable.set("client_id", "204243348983-16vq49624fn347gc75idi5iq6l5rb96b.apps.googleusercontent.com");
		variable.set("client_secret", "US_nFgMNO-NMWBNdpIJ3Hl1u");
		variable.set("redirect_uri", Redirect_Uri);
		variable.set("grant_type", "authorization_code");
		
		HttpEntity<?> entity = new HttpEntity<Object>(variable, headers);
		String body = StringUtil.EMPTY_STRING;
		ResponseEntity<String> responseEntity = null;
		String accessToken = StringUtil.EMPTY_STRING;
		try {
			responseEntity = restTemplate.postForEntity("https://accounts.google.com/o/oauth2/token", entity, String.class);
			body = responseEntity.getBody();
			JSONObject retJSON = new JSONObject(body);
			accessToken = retJSON.getString("access_token");
		} catch (HttpClientErrorException e) {
			body = e.getResponseBodyAsString();
		}

		responseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v1/userinfo?access_token="+accessToken, String.class);
		body = responseEntity.getBody();
		JSONObject retJSON = new JSONObject(body);
		String googleId = retJSON.getString("id");
        String email = retJSON.getString("email");
        String name = retJSON.getString("name");
        String firstName = retJSON.getString("given_name");
        String lastName = retJSON.getString("family_name");
        Cust googleCust = new Cust();
        googleCust.setOid(CommonUtils.getUUIDString());
        googleCust.setCustId(googleId);
        googleCust.setCustName(name);
        googleCust.setEmail(email);
        googleCust.setSex("N");
        googleCust.setPwd(new BCryptPasswordEncoder().encode(""));
        googleCust.setCreDate(new Timestamp(System.currentTimeMillis()));
        googleCust.setAuthority("ROLE_USER");
		// 預設未啟用
        googleCust.setStatus(true);
        googleCust.setProvider("google");
        custServcie.registerGoogleCust(googleCust);
        
		String token = tokenService.getAccessToken(googleId, "");
		Map<String, Object> map = new HashMap<>();
		map.put("access_token", token);
		
		Cust cust = custServcie.getCustById(googleCust.getCustId());
        map.put("custId", googleCust.getCustId());
		if (null != cust) {
		    // 帳號未啟用不給登入
		    if (!cust.getStatus()) {
		        return new BaseResult(CommonEnum.ACCOUNT_NOT_ENABLE);
            }
		    map.put("role", cust.getAuthority());
		    map.put("name", cust.getCustName());
        }
		
		return new BaseResult(CommonEnum.SUCCESS.getCode(), "success", map);
		
	}
	
//	@PostMapping("activeCust")
//	public BaseResult activeCust(String encryCustId) {
//		Cust cust;
//		try {
//			cust = custServcie.getCustById(DESUtil.decodeValue(DESUtil.DES_REG_KEY, encryCustId));
//		} catch (Exception e) {
//			throw new InfoException("帳號啟動失敗");
//		}
//		cust.setStatus(true);
//		custServcie.save(cust);
//		return new BaseResult(CommonEnum.SUCCESS.getCode(), "success");
//	}

    @GetMapping("resendMail")
    public BaseResult resendMail(String custId,String lang) {
        custServcie.sendMail(custId,lang);
        return new BaseResult(CommonEnum.SUCCESS);
    }
    
    @GetMapping("verifyCust")
    public BaseResult verifyCust(String custId, String verifyCode) {
        custServcie.verifyCust(custId, verifyCode);
        return new BaseResult(CommonEnum.SUCCESS);
    }
    
    @PostMapping("sendMessage")
    public BaseResult sendMessage(String name, String email, String subject, String message) {
        
        if (StringUtils.isBlank(name) || StringUtils.isBlank(email) || StringUtils.isBlank(subject) || StringUtils.isBlank(message)) {
            return new BaseResult(CommonEnum.PARAM_NOT_NULL);
        }
        
        StringBuffer msg = new StringBuffer();
        msg.append("姓名:");
        msg.append(name);
        msg.append("\n");
        msg.append("電子信箱:");
        msg.append(email);
        msg.append("\n");
        msg.append("訊息:");
        msg.append("\n");
        msg.append(message);
        mailService.sendSimpleMessage("ad@coin168.cc", "客戶來信: " + subject, msg.toString());
        
        custServcie.saveContact(name, email, subject, message);
        return new BaseResult(CommonEnum.SUCCESS);
    }
    
    
}
