package com.topsci.service;

import java.io.Serializable;
import java.util.List;

import com.topsci.utils.Pager;
import com.topsci.utils.Pager.OrderType;

public interface BaseService<T, PK extends Serializable> {

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
     * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
     *
     * @param propertyName
     *        属性名称
     * @param oldValue
     *        修改前的属性值
     * @param newValue
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
     * 根据Page对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Page对象
     * @return Page对象
     */
    public Pager findByPager(Pager pager);

    /**
     * 根据Pager和DetachedCriteria对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Pager对象
     * @return Pager对象
     */
//	public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria);

    /**
     * 根据属性名判断数据是否已存在.
     *
     * @param entity
     *        值
     * @return boolean
     */
    public boolean isExist(T entity);

    /**
     * 根据Page对象和参数bean进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *        Page对象
     * @param entity
     *        参数bean
     * @return Page对象
     */
    public Pager findByPagerNumByEntity(Integer pager, T entity);

    /**
     * 通过SQL语句修改数据
     *
     * @param sql
     */
    public void updateBySql(String sql);

    /**
     * 根据entity参数查询对应的数据_等于查询
     *
     * @param entity
     * @param addHql
     * @return
     */
    public List<T> findEntityByEntityParams_equals(T entity, String addHql);

    /**
     * 根据entity参数查询对应的数据_模糊查询
     *
     * @param entity
     * @return
     */
    public List<T> findEntityByEntityParams_like(T entity);

    /**
     * 通过hql语句查询
     *
     * @param hql
     * @return
     */
    public List<?> findEntityByHql(String hql);

    /**
     * 赋值entity
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

    /**
     * @description 指定字段排序 list分页查询
     * @param pagerNum
     * @param entity
     * @param columnName
     * @param orderType
     * @return
     */
    public Pager findByPagerNumByEntity(Integer pagerNum, T entity,
            String columnName, OrderType orderType);

    public Pager queryEntityByHql_Page(String hql, Pager page);

    public String saveOrUpdate(T entity) throws Exception;

    public Object findByExample(final Class clazz, final Object obj);

    public Pager findEntityByEntityParamsByPager_equals(T entity,
            String serviceAddHql, Pager pager);
}
