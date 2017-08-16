package com.topsci.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.topsci.dao.BaseDao;
import com.topsci.utils.CommonUtil;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.Pager;
import com.topsci.utils.Pager.OrderType;

public abstract class BaseDaoImpl<T, PK extends Serializable>
        implements BaseDao<T, PK> {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private Class<T> entityClass;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public BaseDaoImpl() {
        this.entityClass = null;
        Class c = this.getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type)
                .getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        }
    }

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     * 根据ID获取实体对象.
     *
     * @param id
     *        记录ID
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        Assert.notNull(id, "id is required");
        return (T) this.getSession().get(this.entityClass, id);
    }

    /**
     * 根据ID获取实体对象.
     *
     * @param id
     *        记录ID
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public T load(PK id) {
        Assert.notNull(id, "id is required");
        try {
            return (T) this.getSession().load(this.entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据ID数组获取实体对象集合.
     *
     * @param ids
     *        ID对象数组
     * @return 实体对象集合
     */
    @SuppressWarnings("unchecked")
    public List<T> get(PK[] ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        String hql = "from " + this.entityClass.getName()
            + " as model where model.id in(:ids)";
        return this.getSession().createQuery(hql).setParameterList("ids", ids)
            .list();
    }

    /**
     * 根据属性名和属性值获取实体对象.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        属性值
     * @return 实体对象
     */
    @SuppressWarnings("unchecked")
    public T get(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + this.entityClass.getName()
            + " as model where model." + propertyName + " = ?";
        return (T) this.getSession().createQuery(hql).setParameter(0, value)
            .uniqueResult();
    }

    /**
     * 根据属性名和属性值获取实体对象集合.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        属性值
     * @return 实体对象集合
     */
    @SuppressWarnings("unchecked")
    public List<T> getList(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + this.entityClass.getName()
            + " as model where model." + propertyName + " = ?";
        return this.getSession().createQuery(hql).setParameter(0, value).list();
    }

    /**
     * 获取所有实体对象集合.
     *
     * @return 实体对象集合
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        String hql = "from " + this.entityClass.getName();
        return this.getSession().createQuery(hql).list();
    }

    /**
     * 获取所有实体对象总数.
     *
     * @return 实体对象总数
     */
    public Long getTotalCount() {
        String hql = "select count(*) from " + this.entityClass.getName();
        return (Long) this.getSession().createQuery(hql).uniqueResult();
    }

    /**
     * 根据HQL条件来查询实体集合
     *
     * @param hql
     * @return
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<?> findEntityByHQL(String hql) throws DataAccessException {
        return this.getSession().createQuery(hql).list();
    }

    /**
     * 根据HQL来查询实体集合
     *
     * @param value
     * @return
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHQL(String hql, Object value)
            throws DataAccessException {
        return this.getSession().createQuery(hql).setParameter(0, value).list();
    }

    /**
     * 根据HQL条件来查询实体集合
     *
     * @param hql
     * @param values
     * @return
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHQL(String hql, Object[] values)
            throws DataAccessException {
        Query query = this.getSession().createQuery(hql);
        int length = values.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query.list();
    }

    /**
     * @describle 执行sql语句查询
     * @param sql
     * @return
     */
    public List<?> findEntityBySql(String sql) {
        List<?> list = this.getSession().createSQLQuery(sql).list();
        if (null == list || list.size() == 0) {
            return null;
        }
        return list;
    }

    /**
     * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
     *
     * @param propertyName
     *        属性名称
     * @param oldValue
     *        修改前的属性值
     * @return boolean
     */
    public boolean isUnique(String propertyName, Object oldValue,
            Object newValue) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(newValue, "newValue is required");
        if (newValue == oldValue || newValue.equals(oldValue)) {
            return true;
        }
        if (newValue instanceof String) {
            if (oldValue != null && oldValue.equals(newValue)) {
                return true;
            }
        }
        T object = this.get(propertyName, newValue);
        return object == null;
    }

    /**
     * 根据属性名判断数据是否已存在.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        值
     * @return boolean
     */
    public boolean isExist(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        T object = this.get(propertyName, value);
        return object != null;
    }

    /**
     * 保存实体对象.
     *
     * @param entity
     *        对象
     * @return ID
     */
    @SuppressWarnings("unchecked")
    public PK save(T entity) {
        Assert.notNull(entity, "entity is required");
        return (PK) this.getSession().save(entity);
    }

    /**
     * 更新实体对象.
     *
     * @param entity
     *        对象
     * @return
     */
    public boolean update(T entity) {
        Assert.notNull(entity, "entity is required");
        try {
            this.getSession().update(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @describle 使用sql语句更新
     * @param sql
     */
    public void updateBySql(String sql) {
        this.getSession().createSQLQuery(sql).executeUpdate();
    }

    /**
     * 删除实体对象.
     *
     * @param entity
     *        对象
     * @return
     */
    public boolean delete(T entity) {
        Assert.notNull(entity, "entity is required");
        try {
            this.getSession().delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据ID删除实体对象.
     *
     * @param id
     *        记录ID
     * @return
     */
    public boolean delete(PK id) {
        Assert.notNull(id, "id is required");
        T entity = this.load(id);
        try {
            this.getSession().delete(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据ID数组删除实体对象.
     *
     * @param ids
     *        ID数组
     */
    public void delete(PK[] ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        for (PK id : ids) {
            T entity = this.load(id);
            this.getSession().delete(entity);
        }
    }

    /**
     * 刷新session.
     */
    public void flush() {
        this.getSession().flush();
    }

    /**
     * 清除Session.
     */
    public void clear() {
        this.getSession().clear();
    }

    /**
     * 清除某一对象.
     *
     * @param object
     *        需要清除的对象
     */
    public void evict(Object object) {
        Assert.notNull(object, "object is required");
        this.getSession().evict(object);
    }

    /**
     * 根据Pager对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Pager对象
     * @return Pager对象
     */
    public Pager findByPager(Pager pager) {
        if (pager == null) {
            pager = new Pager();
        }
        String className = this.entityClass.getName();
        StringBuffer countHql = new StringBuffer(
            "select count(*) from " + className + " where 1=1 ");
        StringBuffer hqlBuffer = new StringBuffer(
            "from " + className + " where 1=1 ");
        if (pager.getLikeObject() != null) {
            Map<String, Object> map = CommonUtil
                .parseParamsByEntity(pager.getLikeObject());
            if (map.size() > 0) {
                Set<String> set = map.keySet();
                for (String property : set) {
                    if (property.indexOf("|DATE") != -1) {
                        String prop = property.substring(0,
                            property.indexOf("|DATE"));
                        hqlBuffer
                            .append(" and to_char(" + prop
                                + ",'yyyy-mm-dd') = '" + DateTimeUtils
                                    .validateDate((Date) map.get(property))
                                + "'");
                        countHql
                            .append(" and to_char(" + prop
                                + ",'yyyy-mm-dd') = '" + DateTimeUtils
                                    .validateDate((Date) map.get(property))
                                + "'");
                    } else {
                        hqlBuffer.append(" and " + property + " like '%"
                            + map.get(property) + "%'");
                        countHql.append(" and " + property + " like '%"
                            + map.get(property) + "%'");
                    }
                }
            }
        }

        int count = Integer.parseInt(this.getSession()
            .createQuery(countHql.toString()).iterate().next().toString());
        Query query = this.getSession().createQuery(hqlBuffer.toString())
            .setFirstResult((pager.getPageNumber().intValue() - 1)
                * pager.getPageSize().intValue());
        query.setMaxResults(pager.getPageSize().intValue());
        pager.setList(query.list());
        pager.setTotalCount(Integer.valueOf(count));
        return pager;
    }

    /**
     * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Pager对象
     * @return Pager对象
     */
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria) {
        try {
            Integer pageNumber = pager.getPageNumber();
            Integer pageSize = pager.getPageSize();
            String orderBy = pager.getOrderBy();
            OrderType orderType = pager.getOrderType();

            Criteria criteria = detachedCriteria
                .getExecutableCriteria(this.getSession());
            /*
             * if(null != pager.getLikeObject()){ Map<String, Object> map =
             * CommonUtil.parseParamsByEntity(pager.getLikeObject());
             * if(map.size() > 0){ Set<String> set = map.keySet(); for(String
             * property : set){ criteria.add(Restrictions.like(property, "%" +
             * map.get(property) + "%")); } } }
             */
            /*
             * if (StringUtils.isNotEmpty(property) &&
             * StringUtils.isNotEmpty(keyword)) { String propertyString = ""; if
             * (property.contains(".")) { String propertyPrefix =
             * StringUtils.substringBefore(property, "."); String propertySuffix
             * = StringUtils.substringAfter(property, ".");
             * criteria.createAlias(propertyPrefix, "model"); propertyString =
             * "model." + propertySuffix; } else { propertyString = property; }
             * criteria.add(Restrictions.like(propertyString, "%" + keyword +
             * "%")); }
             */
            // Integer totalCount = (Integer)
            // criteria.setProjection(Projections.rowCount()).uniqueResult();
            Integer totalCount = Integer
                .parseInt(criteria.setProjection(Projections.rowCount())
                    .uniqueResult().toString());
            criteria.setProjection(null);
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
            criteria.setFirstResult((pageNumber - 1) * pageSize);
            criteria.setMaxResults(pageSize);
            if (orderType != null) {
                if (orderType == OrderType.asc) {
                    criteria.addOrder(Order.asc(orderBy));
                } else {
                    criteria.addOrder(Order.desc(orderBy));
                }
            }
            pager.setTotalCount(totalCount);
            pager.setList(criteria.list());
            // pager.setList(getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pager;
    }

    /**
     * @describle 根据entity实体判断是否存在
     * @date 2013-5-8 上午10:22:52
     */
    public boolean isExist(T entity) {
        Map<String, Object> map = CommonUtil.parseParamsByEntity(entity);
        DetachedCriteria dc = DetachedCriteria.forClass(this.entityClass);
        if (map != null && map.size() > 0) {
            for (Entry<String, Object> entry : map.entrySet()) {
                dc.add(Restrictions.eq(entry.getKey(), entry.getValue()));
            }
        }
        Criteria criteria = dc.getExecutableCriteria(this.getSession());
        Object obj = criteria.uniqueResult();
        return obj == null ? false : true;
    }

    @SuppressWarnings("unchecked")
    public List<T> get(Collection<PK> ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        String hql = "from " + this.entityClass.getName()
            + " as model where model.id in(:ids)";
        return this.getSession().createQuery(hql).setParameterList("ids", ids)
            .list();
    }

    /**
     * 根据hql语句查询对应的一个实体
     *
     * @param hql
     * @return
     */
    @SuppressWarnings("unchecked")
    public T findUniqueEntityByHQL(String hql) {
        T entity = null;
        try {
            entity = (T) this.getSession().createQuery(hql).uniqueResult();
        } catch (Exception e) {
            this.log.error("findUniqueEntityByHQL ERROR: hql:{}", hql);
            this.log.error("Exception:", e);
        }
        return entity;
    }

    /**
     * 赋值实体
     */
    public void setEntityClass(T entity) {
        this.entityClass = (Class<T>) entity.getClass();
    }

    /**
     * 执行hql语句
     *
     * @param hql
     * @return
     */
    public Integer executeHql(String hql) {
        Integer flg = this.getSession().createQuery(hql).executeUpdate();
        return flg;
    }

    public Pager queryEntityByHql_Page(String hql, Pager page) {
        Query query = this.getSession().createQuery(hql);
        if (page != null) {
//			System.err.println("query:pageSize="+page.getPageSize()+" | startIndex="+page.getPageSize()*(page.getPageNumber()-1));
            if (!page.getLogicalPaging()) {
                query.setMaxResults(page.getPageSize()).setFirstResult(
                    page.getPageSize() * (page.getPageNumber() - 1));
            }
        } else {
            page = new Pager();
        }
        page.setList(query.list());
        //.......总数据条数
        hql.substring(hql.indexOf("from"), hql.length());
        hql = "select count(*) " + hql;
        List<?> totalCount = this.getSession().createQuery(hql).list();
        if (totalCount != null && totalCount.size() > 0) {
            page.setTotalCount(((Number) totalCount.get(0)).intValue());
        }
        /////////
        return page;
    }

    public void saveOrUpdate(T entity) {
        this.getSession().saveOrUpdate(entity);
    }

    public Object findByExample(final Class clazz, final Object obj) {
        return this.getSession().createCriteria(clazz.getName())
            .add(Example.create(obj)).uniqueResult();
    }
}
