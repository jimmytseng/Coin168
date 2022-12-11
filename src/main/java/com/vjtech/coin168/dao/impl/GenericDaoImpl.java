package com.vjtech.coin168.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.Assert;

import com.vjtech.coin168.common.result.resp.PageData;
import com.vjtech.coin168.dao.GenericDao;
import com.vjtech.coin168.dto.PageRequest;
import com.vjtech.coin168.model.DataObject;

public class GenericDaoImpl<T> implements GenericDao<T> {

    protected Class<T> type;
    protected Logger logger;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        try {
            type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        } catch (ClassCastException e) {
            Class<T> clazz = (Class<T>) getClass().getGenericSuperclass();
            type = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
        }
        logger = LoggerFactory.getLogger(getClass());
    }

    /**
     * Insert.
     * 
     * @param entity
     *            the entry
     */
    public void save(Object entity) {
        if (null == entity) {
            return;
        }
        if (getEntityManager().contains(entity)) {
            getEntityManager().merge(entity);
        } else {
            getEntityManager().persist(entity);
        }
    }

    public void save(List<?> entries) {
        if (CollectionUtils.isEmpty(entries)) {
            return;
        }
        for (Object entity : entries) {
            if (null == entity) {
                continue;
            }
            save(entity);
        }
    }

    public void merge(T entity) {
        Assert.notNull(entity, "The entity to save cannot be null element");
        getEntityManager().merge(entity);
    }

    /**
     * Delete.
     * 
     * @param entity
     *            the entry
     */
    public void delete(Object entity) {
        if (getEntityManager().contains(entity)) {
            getEntityManager().remove(entity);
        } else {
            // could be a delete on a transient instance
            T entityRef = getEntityManager().getReference(type, getPrimaryKey(entity));

            if (entityRef != null) {
                getEntityManager().remove(entityRef);
            }
        }
    }

    public void delete(List<?> entries) {
        for (Object entity : entries) {
            delete(entity);
        }
    }

    /**
     * Find.
     * 
     * @param pk
     *            the oid
     * 
     * @return the t
     */
    public T find(Serializable pk) {
        return getEntityManager().find(type, pk);
    }

    public Serializable getPrimaryKey(Object model) {
        if (model instanceof DataObject) {
            return (Serializable) ((DataObject) model).getOid();
        } else {
            return null;
        }
    }

    public T find(T entity) {
        Serializable pk = getPrimaryKey(entity);
        if (pk == null) {
            return null;
        }
        return (T) getEntityManager().find(type, pk);
    }

    public Class<T> getType() {
        return type;
    }

    @SuppressWarnings("rawtypes")
    public GenericDaoImpl setType(Class<T> type) {
        this.type = type;
        return this;
    }

    public void flush() {
        getEntityManager().flush();
        getEntityManager().clear();
    }

    public <S> S findById(Class<S> clazz, Serializable pk) {
        return getEntityManager().find(clazz, pk);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public List<T> findAll() {
        String str = "";

        str = "select " + type.getSimpleName() + "  from " + type.getSimpleName() + " " + type.getSimpleName();
        return getEntityManager().createQuery(str, type).getResultList();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List<T> findByParams(String sql, Map<String, Object> params) {
        Query query = getEntityManager().createNativeQuery(sql, type);
        if (null != params && !params.isEmpty()) {
            for (Entry entry : params.entrySet()) {
                query.setParameter(entry.getKey().toString(), entry.getValue());
            }
        }
        return (List<T>) query.getResultList();
    }

    /**
     *
     * 若有排序 ORDER BY 要大寫
     */
    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public PageData findPageDataByParams(String sql, Map<String, Object> params, PageRequest pageRequest) {

        int pageNumber = pageRequest.getPageNumber() - 1;
        int pageSize = pageRequest.getCount();
        StringBuffer sb = new StringBuffer(sql);
        sb.append(" offset ");
        sb.append((pageNumber) * pageSize);
        sb.append(" rows fetch next ");
        sb.append(pageSize);
        sb.append(" rows only");
        List<T> resultList = getNamedJdbcTemplate().query(sb.toString(), params, new BeanPropertyRowMapper<T>(type));
        
        
//        Query query = getEntityManager().createNativeQuery(sql, type);
//        query.setFirstResult((pageNumber) * pageSize);
//        query.setMaxResults(pageSize);
//        if (null != params && !params.isEmpty()) {
//            for (Entry entry : params.entrySet()) {
//                query.setParameter(entry.getKey().toString(), entry.getValue());
//            }
//        }
//        List<T> resultList = (List<T>) query.getResultList();
        

        String sqlWithoutOrder = sql.split("ORDER BY")[0];

        StringBuilder countSqlBulider = new StringBuilder();
        countSqlBulider.append("select count(*) from ( ");
        countSqlBulider.append(sqlWithoutOrder);
        countSqlBulider.append(") temp");
        int totalCount = getNamedJdbcTemplate().queryForObject(countSqlBulider.toString(), params, new SingleColumnRowMapper<Integer>(Integer.class));

        PageData pageData = new PageData();
        pageData.setPagetRest(pageRequest);
        pageData.setData(resultList);
        pageData.setTotalCount(totalCount);
        return pageData;
    }

    protected void createSqlForIn(StringBuilder sb, Map<String, Object> params, String columnName, Object[] values) {

        if (ArrayUtils.isNotEmpty(values)) {
            sb.append(" AND " + columnName + " IN (");

            for (int i = 0; i < values.length; i++) {
                String key = columnName + i;
                Object val = values[i];

                if (i > 0) {
                    sb.append(",");
                }

                sb.append(":" + key);
                params.put(key, val);
            }
            sb.append(")");

        }
    }

    @Override
    public <S> List<S> find(String sql, Map<String, Object> params, Class<S> clazz) {
        Query query = getEntityManager().createNativeQuery(sql, clazz);
        if (null != params && !params.isEmpty()) {
            for (Entry entry : params.entrySet()) {
                query.setParameter(entry.getKey().toString(), entry.getValue());
            }
        }
        return (List<S>) query.getResultList();
    }

    @Override
    public <S> List<S> queryForObject(String sql, Map<String, ?> params, RowMapper<S> rm) {
        return getNamedJdbcTemplate().query(sql, params, rm);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public T getByParams(String sql, Map<String, Object> params) {
        Query query = getEntityManager().createNativeQuery(sql, type);
        if (null != params && !params.isEmpty()) {
            for (Entry entry : params.entrySet()) {
                query.setParameter(entry.getKey().toString(), entry.getValue());
            }
        }

        List<T> list = (List<T>) query.getResultList();
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

}
