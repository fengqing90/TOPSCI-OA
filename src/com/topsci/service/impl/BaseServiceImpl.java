package com.topsci.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.transaction.annotation.Transactional;

import com.topsci.dao.BaseDao;
import com.topsci.service.BaseService;
import com.topsci.utils.CommonUtil;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.Pager;
import com.topsci.utils.Pager.OrderType;

@Transactional
public abstract class BaseServiceImpl<T, PK extends Serializable>
        implements BaseService<T, PK> {

    protected Pager pager = null;

    protected BaseDao<T, PK> baseDao;

    public BaseDao<T, PK> getBaseDao() {
        return this.baseDao;
    }

    public void setBaseDao(BaseDao<T, PK> baseDao) {
        this.baseDao = baseDao;
    }

    public T get(PK id) {
        return this.baseDao.get(id);
    }

    public T load(PK id) {
        return this.baseDao.load(id);
    }

    public List<T> get(PK[] ids) {
        return this.baseDao.get(ids);
    }

    public T get(String propertyName, Object value) {
        return this.baseDao.get(propertyName, value);
    }

    public List<T> getList(String propertyName, Object value) {
        return this.baseDao.getList(propertyName, value);
    }

    public List<T> getAll() {
        return this.baseDao.getAll();
    }

    public Long getTotalCount() {
        return this.baseDao.getTotalCount();
    }

    public boolean isUnique(String propertyName, Object oldValue,
            Object newValue) {
        return this.baseDao.isUnique(propertyName, oldValue, newValue);
    }

    public boolean isExist(String propertyName, Object value) {
        return this.baseDao.isExist(propertyName, value);
    }

    public PK save(T entity) {
        return this.baseDao.save(entity);
    }

    public boolean update(T entity) {
        return this.baseDao.update(entity);
    }

    public boolean delete(T entity) {
        return this.baseDao.delete(entity);
    }

    public boolean delete(PK id) {
        return this.baseDao.delete(id);
    }

    public void delete(PK[] ids) {
        this.baseDao.delete(ids);
    }

    public Pager findByPager(Pager pager) {
//		try {
//			return baseDao.findByPager(pager);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return null;
    }

    public boolean isExist(T entity) {
        return this.baseDao.isExist(entity);
    }

    public Pager findByPagerNumByEntity(Integer pagerNum, T entity) {
        if (null == this.pager) {
            this.pager = new Pager();
        }
        this.pager.setPageNumber(pagerNum);
        this.pager.setLikeObject(entity);
        return this.findByPager(this.pager);
    }

    public void updateBySql(String sql) {
        try {
            this.baseDao.updateBySql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据entity参数查询对应的数据_模糊查询
     *
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> findEntityByEntityParams_like(T entity) {
        Pager pager = new Pager();
        pager.setPageSize(Integer.MAX_VALUE);
        pager.setLikeObject(entity);
//		pager = baseDao.findByPager(pager);
        return (List<T>) pager.getList();
    }

    /**
     * 通过hql语句查询实体entity
     *
     * @param hql
     */
    public List<?> findEntityByHql(String hql) {
        return this.baseDao.findEntityByHQL(hql);
    }

    public void setEntityClass(T entity) {
        this.baseDao.setEntityClass(entity);
    }

    /**
     * 执行hql语句
     */
    public Integer executeHql(String hql) {
        return this.baseDao.executeHql(hql);
    }

    /**
     * @description 指定字段排序 list分页查询
     * @param pagerNum
     * @param entity
     * @param columnName
     * @param orderType
     * @return
     */
    public Pager findByPagerNumByEntity(Integer pagerNum, T entity,
            String columnName, OrderType orderType) {
        if (null == this.pager) {
            this.pager = new Pager();
        }
        this.pager.setPageNumber(pagerNum);
        this.pager.setLikeObject(entity);
//		pager.setColumnName(columnName);
        this.pager.setOrderType(orderType);
        return this.findByPager(this.pager);
    }

    public Pager queryEntityByHql_Page(String hql, Pager page) {
        return this.baseDao.queryEntityByHql_Page(hql, page);
    }

    public String saveOrUpdate(T entity) throws Exception {
        this.baseDao.saveOrUpdate(entity);
        return "success,修改 Or 新增 成功！";
    }

    public Object findByExample(Class clazz, Object obj) {
        return this.baseDao.findByExample(clazz, obj);
    }

    public List<T> findEntityByEntityParams_equals(T entity, String addHql) {
        Map<String, Object> map = CommonUtil.parseParamsByEntity(entity);
        StringBuffer hql = new StringBuffer();
        hql.append("from " + entity.getClass().getName());
        if (map.size() > 0) {
            hql.append(" where 1=1 ");
            for (Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    hql.append(" and " + entry.getKey() + " = '" + value + "'");
                } else if (value instanceof Date) {
                    hql.append(" and date_format(" + entry.getKey()
                        + ",'%Y-%m-%d') = '"
                        + DateTimeUtils.validateDate((Date) entry.getValue())
                        + "'");
                }
                //.............................
            }
        }
        return (List<T>) this.baseDao.findEntityByHQL(hql.toString());
    }

    public Pager findEntityByEntityParamsByPager_equals(T entity,
            String serviceAddHql, Pager pager) {
        Map<String, Object> map = CommonUtil.parseParamsByEntity(entity);
        StringBuffer hql = new StringBuffer();
        hql.append("from " + entity.getClass().getName() + " where 1=1 ");
        if (map.size() > 0) {
            for (Entry<String, Object> entry : map.entrySet()) {
                hql.append(" and " + entry.getKey() + "=");
                Object value = entry.getValue();
                if (value instanceof String) {
                    hql.append(" and " + entry.getKey() + " = '" + value + "'");
                } else if (value instanceof Date) {
                    hql.append(" and date_format(" + entry.getKey()
                        + ",'%Y-%m-%d') = '"
                        + DateTimeUtils.validateDate((Date) entry.getValue())
                        + "'");
                }
                //.............................
            }
        }

        if (serviceAddHql != null) {
            hql.append(serviceAddHql);
        }
        return this.baseDao.queryEntityByHql_Page(hql.toString(), pager);
    }
}