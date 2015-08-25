package com.dm.platform.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Sort.Order;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dm.platform.dao.CommonDAO;

@Repository
public class CommonDAOImpl extends HibernateDaoSupport implements CommonDAO {
	@SuppressWarnings("unused")
	private SessionFactory sessionFacotry;

	@Resource
	public void setSessionFacotry(SessionFactory sessionFacotry) {
		super.setSessionFactory(sessionFacotry);
	}

	protected void initDao() {
	}

	@Override
	public <T> void save(T entity) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().save(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> void update(T entity) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().update(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> void delete(T entity) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().delete(entity);
		} catch (RuntimeException re) {
			throw re;
		}

	}

	@Override
	public <T> void merge(T entity) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void execute(final String queryString) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().execute(new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
					Query query = session.createQuery(queryString);
					return query.executeUpdate();
			 }
			});
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public <T> T findOne(Class<T> entityClass, Object id) {
		// TODO Auto-generated method stub
		try {
			return (T) getHibernateTemplate().get(entityClass,
					(Serializable) id);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findByPropertyName(Class<T> entityClass, String name,
			Object value) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName()
					+ " t where t." + name + " = ?";
			return (List<T>) getHibernateTemplate().find(queryString,
					value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public <T> List<T> findByPropertyName(Class<T> entityClass, String name,
			Object value,List<Order> orders) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName()
					+ " t where t." + name + " = ?";
			
			int count = 0;
			for (Order order : orders) {
				if (count < 1) {
					if (order.getDirection().ordinal() == 0) {
						queryString += " order by " + order.getProperty()
								+ " asc";
					} else {
						queryString += " order by " + order.getProperty()
								+ " desc";
					}
					count++;
				} else {
					if (order.getDirection().ordinal() == 0) {
						queryString += " , " + order.getProperty() + " asc";
					} else {
						queryString += " , " + order.getProperty() + " desc";
					}
					count++;
				}
			}
			
			return (List<T>) getHibernateTemplate().find(queryString,
					value);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	@Override
	public <T> List<T> findByPropertyName(Class<T> entityClass, String[] names,
			Object[] values,List<Order> orders) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName() + "t where 1=1";
			for (String name : names) {
				queryString+= " and t." + name + " = ?";
			}
			
			int count = 0;
			for (Order order : orders) {
				if (count < 1) {
					if (order.getDirection().ordinal() == 0) {
						queryString += " order by " + order.getProperty()
								+ " asc";
					} else {
						queryString += " order by " + order.getProperty()
								+ " desc";
					}
					count++;
				} else {
					if (order.getDirection().ordinal() == 0) {
						queryString += " , " + order.getProperty() + " asc";
					} else {
						queryString += " , " + order.getProperty() + " desc";
					}
					count++;
				}
			}
			
			
			return (List<T>) getHibernateTemplate().find(queryString,
					values);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@Override
	public <T> List<T> findByPropertyName(Class<T> entityClass, String[] names,
			Object[] values) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName() + " t where 1=1";
			for (String name : names) {
				queryString+= " and t." + name + " = ?";
			}
			return (List<T>) getHibernateTemplate().find(queryString,
					values);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String queryString) {
		// TODO Auto-generated method stub
		try {
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> find(final String queryString,final T entity) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
					Query query = session.createQuery(queryString);
					if(entity!=null)
						query.setProperties(entity);
					return (List<T>)query.list(); 
			 }
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByMap(final String sql,final Map argsMap) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
					Query query = session.createQuery(sql);
					query.setProperties(argsMap);
					return (List<T>)query.list(); 
			 }
		});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName();
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findAll(final Class<T> entityClass, final String whereSql, final T entity) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback ( ) {
				 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
					 String queryString = "from " + entityClass.getName()
								+ " t where 1=1 ";
						if (whereSql != null && !whereSql.equals("")) {
							queryString += whereSql;
						}
						Query query = session.createQuery(queryString);
						if (whereSql != null && !whereSql.equals("")) {
							query.setProperties(entity);
						}
						return query.list(); 
				 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findAll(final Class<T> entityClass, final String whereSql, final T entity,
			final List<Order> orders) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = "from " + entityClass.getName()
							+ " t where 1=1 ";
					if (whereSql != null && !whereSql.equals("")) {
						queryString += whereSql;
					}
					int count = 0;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								queryString += " order by " + order.getProperty()
										+ " asc";
							} else {
								queryString += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								queryString += " , " + order.getProperty() + " asc";
							} else {
								queryString += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					Query query = session.createQuery(queryString);
					if (whereSql != null && !whereSql.equals("")) {
						query.setProperties(entity);
					}
					return query.list();
			 }
	});
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entityClass, List<Order> orders) {
		// TODO Auto-generated method stub
		try {
			String queryString = "from " + entityClass.getName();
			int count = 0;
			for (Order order : orders) {
				if (count < 1) {
					if (order.getDirection().ordinal() == 0) {
						queryString += " order by " + order.getProperty()
								+ " asc";
					} else {
						queryString += " order by " + order.getProperty()
								+ " desc";
					}
					count++;
				} else {
					if (order.getDirection().ordinal() == 0) {
						queryString += " , " + order.getProperty() + " asc";
					} else {
						queryString += " , " + order.getProperty() + " desc";
					}
					count++;
				}
			}
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByPage(final String queryString, final int thispage,
			final int pagesize) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback ( ) {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 Query query = session.createQuery(queryString);
				 query.setFirstResult(thispage * pagesize);
				 query.setMaxResults(pagesize);
				 List<T> list = (List<T>) query.list();
				 return list;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByPage(final String whereSql, final Class<T> entityClass,final T entity, final int thispage, final int pagesize, final List<Order> orders) {
		return this.getHibernateTemplate().executeFind( new HibernateCallback ( ) {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = "from " + entityClass.getName()
							+ " t where 1=1 ";
					if (whereSql != null && !whereSql.equals("")) {
						queryString += whereSql;
					}
					int count = 0;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								queryString += " order by " + order.getProperty()
										+ " asc";
							} else {
								queryString += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								queryString += " , " + order.getProperty() + " asc";
							} else {
								queryString += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					Query query = session.createQuery(queryString);
					if(entity!=null){
						query.setProperties(entity);
					}
					query.setFirstResult(thispage * pagesize);
					query.setMaxResults(pagesize);
					List<T> list = (List<T>) query.list();
					return list;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByPage(final String queryString, final int thispage,final int pagesize, final List<Order> orders) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback ( ) {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 int count = 0;
				 String sql = queryString;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								sql += " order by " + order.getProperty()
										+ " asc";
							} else {
								sql += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								sql += " , " + order.getProperty() + " asc";
							} else {
								sql += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					Query query = session.createQuery(sql);
					query.setFirstResult(thispage * pagesize);
					query.setMaxResults(pagesize);
					List<T> list = (List<T>) query.list();
					return list;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByPage(final String queryString, final T entity, final int thispage,final int pagesize, final List<Order> orders) {
		// TODO Auto-generated method stub
		return this.getHibernateTemplate().executeFind( new HibernateCallback ( ) {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 int count = 0;
				 String sql=queryString;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								sql += " order by " + order.getProperty()
										+ " asc";
							} else {
								sql += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								sql += " , " + order.getProperty() + " asc";
							} else {
								sql += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					
					Query query = session.createQuery(sql);
					if(entity!=null){
						query.setProperties(entity);
					}
					query.setFirstResult(thispage * pagesize);
					query.setMaxResults(pagesize);
					List<T> list = (List<T>) query.list();
					return list;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Long count(final String queryString) {
		// TODO Auto-generated method stub
		return (Long) this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 Query query = session.createQuery(queryString);
				 Long count = (Long) query.uniqueResult();
				 return count;
			 }
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Long count(final String queryString,final String parameter,final Object[] objects) {
		// TODO Auto-generated method stub
		return (Long) this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 Query query = session.createQuery(queryString);
				 query.setParameterList(parameter, objects);
				 Long count = (Long) query.uniqueResult();
				 return count;
			 }
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Long count(final String queryString,final Map argsMap) {
		// TODO Auto-generated method stub
		return (Long) this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 Query query = session.createQuery(queryString);
				 query.setProperties(argsMap);
				 Long count = (Long) query.uniqueResult();
				 return count;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> Long count(final Class<T> entityClass, final String whereQueryString,
			final T entity) {
		// TODO Auto-generated method stub
		
		return (Long) this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String sqlQuery = "select count(*) from " + entityClass.getName()
							+ " t where 1=1 ";
					if (whereQueryString != null && !whereQueryString.equals("")) {
						sqlQuery += whereQueryString;
					}
					Query query = session.createQuery(sqlQuery);
					if (whereQueryString != null && !whereQueryString.equals("")) {
						query.setProperties(entity);
					}
					Long count = (Long) query.uniqueResult();
					return count;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void deleteById(final Class<T> entityClass, final Object id) {
		 this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = "delete " + entityClass.getName()
							+ " c where c.id = ?";
					Query query = session.createQuery(queryString);
					query.setParameter(0, id);
					query.executeUpdate();
					return 0;
			 }
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByMapArg(final Class<T> entityClass, final String whereSql, final Map map,
			final List<Order> orders) {
		return this.getHibernateTemplate().executeFind( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = "from " + entityClass.getName()
							+ " t where 1=1 ";
					if (whereSql != null && !whereSql.equals("")) {
						queryString += whereSql;
					}
					int count = 0;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								queryString += " order by " + order.getProperty()
										+ " asc";
							} else {
								queryString += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								queryString += " , " + order.getProperty() + " asc";
							} else {
								queryString += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					Query query = session.createQuery(queryString);
					if (whereSql != null && !whereSql.equals("")) {
						query.setProperties(map);
					}
					List<T> result = (List<T>) query.list();
					return result;
			 }
	});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> List<T> findByMapArg(final Class<T> entityClass, final String whereSql, final Map map,
			final List<Order> orders,final int thispage,final int pagesize) {
		return this.getHibernateTemplate().executeFind( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = "from " + entityClass.getName()
							+ " t where 1=1 ";
					if (whereSql != null && !whereSql.equals("")) {
						queryString += whereSql;
					}
					int count = 0;
					for (Order order : orders) {
						if (count < 1) {
							if (order.getDirection().ordinal() == 0) {
								queryString += " order by " + order.getProperty()
										+ " asc";
							} else {
								queryString += " order by " + order.getProperty()
										+ " desc";
							}
							count++;
						} else {
							if (order.getDirection().ordinal() == 0) {
								queryString += " , " + order.getProperty() + " asc";
							} else {
								queryString += " , " + order.getProperty() + " desc";
							}
							count++;
						}
					}
					Query query = session.createQuery(queryString);
					if (whereSql != null && !whereSql.equals("")) {
						query.setProperties(map);
					}
					query.setFirstResult(thispage * pagesize);
					query.setMaxResults(pagesize);
					List<T> result = (List<T>) query.list();
					return result;
			 }
	});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> void deleteBySql(final Class<T> entityClass,final String whereSql,final Map map) {
		 this.getHibernateTemplate().execute( new HibernateCallback () {
			 public Object doInHibernate ( Session session ) throws HibernateException, SQLException {
				 String queryString = " delete " + entityClass.getName()
							+ " t where 1=1 ";
				 	if (StringUtils.isNotEmpty(whereSql)) {
						queryString += whereSql;
					}
					Query query = session.createQuery(queryString);
					if(map!=null)
						query.setProperties(map);
					query.executeUpdate();
					return 0;
			 }
		});
	}

	@Override
	public <T> T loadOne(Class<T> entityClass, Object id) {
		// TODO Auto-generated method stub
		return (T) this.getHibernateTemplate().load(entityClass, (Serializable)id);
	}

	@Override
	public <T> void saveOrUpdate(T entity) {
		// TODO Auto-generated method stub
		try {
			getHibernateTemplate().saveOrUpdate(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}



}
