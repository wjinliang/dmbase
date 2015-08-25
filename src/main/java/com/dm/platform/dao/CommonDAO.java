package com.dm.platform.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort.Order;

public interface CommonDAO{
	public abstract <T> void save(T entity);
	
	public abstract <T> void saveOrUpdate(T entity);
	
	public abstract <T> void update(T entity);

	public abstract <T> void delete(T entity);
	
	public abstract <T> void merge(T entity);
	
	public abstract <T> void execute(String queryString);
	
	public abstract <T> void deleteById(Class<T> entityClass,Object id);
	
	public abstract <T> Long count(String queryString);
	
	public abstract <T> Long count(final String queryString,final String parameter,final Object[] objects);
	
	public abstract <T> Long count(final String queryString,final Map argsMap);
	
	public abstract <T> Long count(Class<T> entityClass,String whereQueryString,T entity);

	public abstract <T> T findOne(Class<T> entityClass, Object id);

	public abstract <T> T loadOne(Class<T> entityClass, Object id);
	
	public abstract <T> List<T> findByPropertyName(Class<T> entityClass,String name,Object value);
	
	public abstract <T> List<T> findByPropertyName(Class<T> entityClass,String[] name,Object[] value);
	
	public abstract <T> List<T> findByPropertyName(Class<T> entityClass,String name,Object value,List<Order> orders);
	
	public abstract <T> List<T> findByPropertyName(Class<T> entityClass,String[] name,Object[] value,List<Order> orders);
	
	public abstract <T> List<T> find(String queryString);
	
	public abstract <T> List<T> find(final String queryString,final T entity);
	
	public abstract <T> List<T> findByMap(final String sql,final Map argsMap);
	
	public abstract <T> List<T> findAll(Class<T> entityClass);
	
	public abstract <T> List<T> findAll(Class<T> entityClass,List<Order> orders);
	
	public abstract <T> List<T> findAll(Class<T> entityClass,String whereSql,T entity);
	
	public abstract <T> List<T> findAll(Class<T> entityClass,String whereSql,T entity,List<Order> orders);
	
	public abstract <T> List<T> findByPage(String queryString,int thispage,int pagesize);
	
	public abstract <T> List<T> findByPage(String queryString,int thispage,int pagesize,List<Order> orders);
	
	public abstract <T> List<T> findByPage(String queryString,T entity, int thispage,int pagesize,List<Order> orders);
	
	public abstract <T> List<T> findByPage(String whereSql,Class<T> entityClass,T entity,int thispage,int pagesize,List<Order> orders);

	public abstract <T> List<T> findByMapArg(final Class<T> entityClass, final String whereSql, Map map,final List<Order> orders) ;
	
	public abstract <T> List<T> findByMapArg(final Class<T> entityClass, final String whereSql, final Map map,
			final List<Order> orders,final int thispage,final int pagesize);
	
	public abstract <T> void deleteBySql(final Class<T> entityClass,final String whereSql,Map argMap) ;

}
