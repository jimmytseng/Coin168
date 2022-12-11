package com.vjtech.coin168.dao.impl;

import org.springframework.stereotype.Repository;

import com.vjtech.coin168.dao.ContactDao;
import com.vjtech.coin168.model.Contact;

@Repository("ContactDao")
public class ContactDaoImpl extends GenericDaoImpl<Contact> implements ContactDao {

}