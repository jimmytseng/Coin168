/*
 *
 * Copyright (c) 2009-2012 International Integrated System, Inc.
 * 11F, No.133, Sec.4, Minsheng E. Rd., Taipei, 10574, Taiwan, R.O.C.
 * All Rights Reserved.
 *
 * Licensed Materials - Property of International Integrated System,Inc.
 *
 * This software is confidential and proprietary information of
 * International Integrated System, Inc. ("Confidential Information").
 */
package com.vjtech.coin168.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dto.PageRequest;

public interface GenericDao<T> {

    void save(Object models);

    void merge(T models);

    /**
     * Insert.
     * 
     * @param models
     *            the entity
     */
    void save(List<?> models);

    /**
     * Delete.
     * 
     * @param entity
     *            the entity
     */
    void delete(Object entity);

    void delete(List<?> entries);

    /**
     * Find.
     * 
     * @param pk
     *            the oid
     * 
     * @return the t
     */
    T find(Serializable pk);

    T find(T entity);

    /**
     * flush
     */
    void flush();

    <S> S findById(Class<S> clazz, Serializable pk);

    List<T> findAll();

    List<T> findByParams(String sql, Map<String, Object> params);

    <S> List<S> find(String sql, Map<String, Object> params, Class<S> clazz);

    <S> List<S> queryForObject(String sql, Map<String, ?> params, RowMapper<S> rm);

    T getByParams(String sql, Map<String, Object> params);

	PageData findPageDataByParams(String sql, Map<String, Object> params, PageRequest pageRequest);
}
