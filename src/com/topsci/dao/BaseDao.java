package com.topsci.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.topsci.utils.Pager;

public interface BaseDao<T, PK extends Serializable> {

    /**
     * 根据ID获取实体对象.
     *
     * @param id
     *        记录ID
     * @return 实体对象
     */
    public T get(PK id);

    /**
     * 根据ID获取实体对象.
     *
     * @param id
     *        记录ID
     * @return 实体对象
     */
    public T load(PK id);

    /**
     * 根据ID数组获取实体对象集合.
     *
     * @param ids
     *        ID对象数组
     * @return 实体对象集合
     */
    public List<T> get(PK[] ids);

    /**
     * 根据属性名和属性值获取实体对象.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        属性值
     * @return 实体对象
     */
    public T get(String propertyName, Object value);

    /**
     * 根据属性名和属性值获取实体对象集合.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        属性值
     * @return 实体对象集合
     */
    public List<T> getList(String propertyName, Object value);

    /**
     * 获取所有实体对象集合.
     *
     * @return 实体对象集合
     */
    public List<T> getAll();

    /**
     * 获取所有实体对象总数.
     *
     * @return 实体对象总数
     */
    public Long getTotalCount();

    /**
     * 根据HQL条件来查询实体集合
     *
     * @param hql
     * @return
     * @throws DataAccessException
     */
    public List<?> findEntityByHQL(String hql) throws DataAccessException;

    /**
     * 根据HQL来查询实体集合
     *
     * @param hql
     * @param value
     * @return
     * @throws DataAccessException
     */
    public List<T> findEntityByHQL(String hql, Object value)
            throws DataAccessException;

    /**
     * 根据HQL条件来查询实体集合
     *
     * @param hql
     * @param values
     * @return
     * @throws DataAccessException
     */
    public List<T> findEntityByHQL(String hql, Object[] values)
            throws DataAccessException;

    /**
     * 执行sql语句查询
     *
     * @param sql
     * @return
     */
    public List<?> findEntityBySql(String sql);

    /**
     * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
     *
     * @param propertyName
     *        属性名称
     * @param oldValue
     *        修改前的属性值
     * @param newValue
     *        修改后的属性值
     * @return boolean
     */
    public boolean isUnique(String propertyName, Object oldValue,
            Object newValue);

    /**
     * 根据属性名判断数据是否已存在.
     *
     * @param propertyName
     *        属性名称
     * @param value
     *        值
     * @return boolean
     */
    public boolean isExist(String propertyName, Object value);

    /**
     * 保存实体对象.
     *
     * @param entity
     *        对象
     * @return ID
     */
    public PK save(T entity);

    /**
     * 更新实体对象.
     *
     * @param entity
     *        对象
     * @return
     */
    public boolean update(T entity);

    /**
     * @describle 使用sql语句更新
     * @param sql
     */
    public void updateBySql(String sql);

    /**
     * 删除实体对象.
     *
     * @param entity
     *        对象
     * @return
     */
    public boolean delete(T entity);

    /**
     * 根据ID删除实体对象.
     *
     * @param id
     *        记录ID
     * @return
     */
    public boolean delete(PK id);

    /**
     * 根据ID数组删除实体对象.
     *
     * @param ids
     *        ID数组
     */
    public void delete(PK[] ids);

    /**
     * 刷新session.
     */
    public void flush();

    /**
     * 清除Session.
     */
    public void clear();

    /**
     * 清除某一对象.
     *
     * @param object
     *        需要清除的对象
     */
    public void evict(Object object);

    /**
     * 根据Pager对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Pager对象
     * @return Pager对象
     */
    public Pager findByPager(Pager pager);

    /**
     * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        对象
     * @param detachedCriteria
     * @return Pager对象
     */
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria);

    /**
     * 根据entityBean判断是否存在
     *
     * @param entity
     * @return
     */
    public boolean isExist(T entity);

    /**
     * 根据ID集合获取实体对象集合.
     *
     * @param ids
     *        ID对象数组
     * @return 实体对象集合
     */
    public List<T> get(Collection<PK> ids);

    /**
     * 根据hql语句查询对应的一个实体
     *
     * @param hql
     * @return
     */
    public T findUniqueEntityByHQL(String hql);

    /**
     * 赋值需要操作的实体
     *
     * @param entity
     */
    public void setEntityClass(T entity);

    /**
     * 执行hql语句
     *
     * @param hql
     * @return
     */
    public Integer executeHql(String hql);

    public Pager queryEntityByHql_Page(String hql, Pager page);

    public void saveOrUpdate(T entity) throws Exception;

    public Object findByExample(final Class clazz, final Object obj);
}