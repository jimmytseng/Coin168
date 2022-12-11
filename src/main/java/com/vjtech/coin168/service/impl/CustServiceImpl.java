package com.vjtech.coin168.service.impl;

import java.sql.Timestamp;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vjtech.coin168.common.result.BaseResult;
import com.vjtech.coin168.common.result.CommonEnum;
import com.vjtech.coin168.dao.ContactDao;
import com.vjtech.coin168.dao.CustDao;
import com.vjtech.coin168.dao.CustVerifyDao;
import com.vjtech.coin168.exception.InfoException;
import com.vjtech.coin168.model.Contact;
import com.vjtech.coin168.model.Cust;
import com.vjtech.coin168.model.CustVerify;
import com.vjtech.coin168.service.CustService;
import com.vjtech.coin168.utils.CommonUtils;
import com.vjtech.coin168.utils.DESUtil;

@Service
public class CustServiceImpl implements CustService {

	private static final Logger logger = LoggerFactory.getLogger(CustServiceImpl.class);

	@Autowired
	private CustDao custDao;
	@Autowired
	private CustVerifyDao custVerifyDao;
	@Autowired
	private ContactDao cntactDao;

	@Autowired
	private MailService mailSercie;

	private static String REGISTER_HTML_TW = "<html lang=\"zh-TW\"><head><div style=\"width: 400px; margin: 0px auto; text-align: left;\"><br><br> $name$ 你好:"
			+ "	<br><br><br>感謝您註冊Coin168！請使用以下驗證碼確認您的電子信箱地址。"
			+ "	<div style=\"padding: 30px 0px; text-align: center;\"><div style=\"color: white; font-size: 14px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; line-height: 150%; width: fit-content; margin: 0px auto; background-color: rgb(156, 214, 122); padding: 15px 50px; border-radius: 100px;\">$code$</div></div></div></head></html>";

	private static String REGISTER_HTML_EN = "<html lang=\"zh-TW\"><head><div style=\"width: 400px; margin: 0px auto; text-align: left;\"><br><br> Hi $name$ :	\r\n"
			+ "					\r\n"
			+ "					<br><br><br>Thank you for your registration！Please activate your account with verification code below。	\r\n"
			+ "								\r\n"
			+ "								<div style=\"padding: 30px 0px; text-align: center;\"><div style=\"color: white; font-size: 14px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; line-height: 150%; width: fit-content; margin: 0px auto; background-color: rgb(156, 214, 122); padding: 15px 50px; border-radius: 100px;\">$code$</div></div></div></head></html>";

	private static String REGISTER_HTML_CN = "<html lang=\"zh-TW\"><head><div style=\"width: 400px; margin: 0px auto; text-align: left;\"><br><br> $name$ 你好:	\r\n"
			+ "					<br><br><br>感谢您注册Coin168！请使用以下验证码确认您的电子邮箱地址。	\r\n"
			+ "								<div style=\"padding: 30px 0px; text-align: center;\"><div style=\"color: white; font-size: 14px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; line-height: 150%; width: fit-content; margin: 0px auto; background-color: rgb(156, 214, 122); padding: 15px 50px; border-radius: 100px;\">$code$</div></div></div></head></html>";

	@Override
	@Transactional
	public BaseResult registerCust(Cust cust, String lang) {
		Cust existCust = custDao.findByCustId(cust.getCustId());
		if (null != existCust) {
			throw new InfoException("Account is already existed.");
		}
		existCust = custDao.findByCustMail(cust.getEmail());
//        if (null != existCust) {
//            throw new InfoException("Eamil is already existed.");
//        }
		Cust newCust = new Cust();
		BeanUtils.copyProperties(cust, newCust);
		newCust.setOid(CommonUtils.getUUIDString());
		newCust.setPwd(new BCryptPasswordEncoder().encode(cust.getPwd()));
		newCust.setCreDate(new Timestamp(System.currentTimeMillis()));
		newCust.setAuthority("ROLE_USER");
		newCust.setProvider("local");
		// 預設未啟用
		newCust.setStatus(false);
		custDao.save(newCust);

		// 儲存驗證碼
		CustVerify custVerify = new CustVerify();
		custVerify.setOid(CommonUtils.getUUIDString());
		custVerify.setVerifyCode(CommonUtils.getVerifyCode());
		custVerify.setCustId(newCust.getCustId());
		custVerify.setCreDate(new Timestamp(System.currentTimeMillis()));
		custVerifyDao.save(custVerify);
		String html = getHTMLTemplateByLang(lang);
		html = html.replace("$name$", newCust.getCustName());
		html = html.replace("$code$", custVerify.getVerifyCode());
		mailSercie.sendWithHtml(cust.getEmail(), getMailTileByLang(lang), html);
		BaseResult result = new BaseResult(CommonEnum.SUCCESS.getCode(), "register success", null);
		return result;
	}
	
	@Transactional
	@Override
	public void registerGoogleCust(Cust cust) {
		Cust existCust = custDao.findByCustId(cust.getCustId());
		if (null != existCust) {
			return ;
		}
		custDao.save(cust);
		return ;
	}

	@Override
	public Cust getCustById(String custId) {
		return custDao.findByCustId(custId);
	}

	@Override
	@Transactional
	public void save(Cust cust) {
		custDao.save(cust);
	}

	@Override
	public void sendMail(String custId, String lang) {
		Cust cust = custDao.findByCustId(custId);
		CustVerify custVerify = custVerifyDao.findByCustId(custId);

		if (null == cust) {
			throw new InfoException("查無使用者資訊");
		}

		if (null == custVerify) {
			throw new InfoException("查無驗證碼資訊");
		}
		String html = getHTMLTemplateByLang(lang);
		html = html.replace("$name$", cust.getCustName());
		html = html.replace("$code$", custVerify.getVerifyCode());

		mailSercie.sendWithHtml(cust.getEmail(),  getMailTileByLang(lang), html);
	}

	@Transactional
	@Override
	public void verifyCust(String custId, String verifyCode) {

		Cust cust = custDao.findByCustId(custId);
		CustVerify custVerify = custVerifyDao.findByCustId(custId);

		if (null == cust) {
			throw new InfoException("查無使用者資訊");
		}

		if (null == custVerify) {
			throw new InfoException("查無驗證碼資訊");
		}

		if (!StringUtils.equals(custVerify.getVerifyCode(), verifyCode)) {
			throw new InfoException("驗證碼錯誤");
		}

		// 修改使用者狀態
		cust.setStatus(true);
		custDao.save(cust);

		// 刪除驗證碼資訊
		custVerifyDao.delete(custVerify);
	}

	@Transactional
	@Override
	public void saveContact(String custName, String email, String subject, String message) {
		Contact contact = new Contact();
		contact.setOid(CommonUtils.getUUIDString());
		contact.setCustName(custName);
		contact.setEmail(email);
		contact.setSubject(subject);
		contact.setMessage(message);
		contact.setCreDate(new Timestamp(System.currentTimeMillis()));
		cntactDao.save(contact);
	}

	private String getHTMLTemplateByLang(String lang) {
		switch (lang) {
		case "cn":
			return REGISTER_HTML_CN;
		case "tw":
			return REGISTER_HTML_TW;
		case "en":
			return REGISTER_HTML_EN;
		default:
			return REGISTER_HTML_CN;
		}
	}

	private String getMailTileByLang(String lang) {
		switch (lang) {
		case "cn":
			return "请确认您的邮箱";
		case "tw":
			return "請確認您的信箱";
		case "en":
			return "Please comfirm your email";
		default:
			return "请确认您的邮箱";
		}
	}
//	public static void main(String args[]) {
//		System.out.println(CustServiceImpl.REGISTER_HTML_TW);
//	}

}
