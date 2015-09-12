package com.dm.platform.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.LogEntity;
import com.dm.platform.service.LogService;
import com.dm.platform.util.DmDateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class LogServiceImpl implements LogService{

	@Resource
	private CommonDAO commonDAO;
	@Resource
	private SqlSessionTemplate sqlSession;
	
	@Override
	public List<LogEntity> listLogEntity(LogEntity le, int thispage, int pagesize) {
		// TODO Auto-generated method stub
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "id"));
		String whereSql="";
		if(le!=null){
			if(le.getType()!=null&&!le.getType().equals("")){
				whereSql+=" and t.type=:type";
			}
			if(le.getContent()!=null&&!le.getContent().equals("")){
				whereSql+=" and t.content  like :content";
				le.setContent("%"+le.getContent()+"%");
			}
			if(le.getIp()!=null&&!le.getIp().equals("")){
				whereSql+=" and t.ip like :ip";
				le.setIp("%"+le.getIp()+"%");
			}
			if(le.getTitle()!=null&&!le.getTitle().equals("")){
				whereSql+=" and t.title like :title";
				le.setTitle("%"+le.getTitle()+"%");
			}
		}
		return commonDAO.findByPage(whereSql,LogEntity.class,le,thispage, pagesize,orders);
	}

	@Override
	public Long countLog(LogEntity le) {
		// TODO Auto-generated method stub
		/*String hql = "select count(*) from  LogEntity l where 1=1";
		if(le.getType()!=null&&!le.getType().equals("")){
			hql+=" and l.type='"+le.getType()+"'";
		}*/
		String whereSql="";
		if(le!=null){
			if(le.getType()!=null&&!le.getType().equals("")){
				whereSql+=" and t.type=:type";
			}
			if(le.getContent()!=null&&!le.getContent().equals("")){
				whereSql+=" and t.content  like :content";
				le.setContent("%"+le.getContent()+"%");
			}
			if(le.getIp()!=null&&!le.getIp().equals("")){
				whereSql+=" and t.ip like :ip";
				le.setIp("%"+le.getIp()+"%");
			}
			if(le.getTitle()!=null&&!le.getTitle().equals("")){
				whereSql+=" and t.title like :title";
				le.setTitle("%"+le.getTitle()+"%");
			}
		}
		return commonDAO.count(LogEntity.class,whereSql,le);
	}

	@Override
	public LogEntity findOne(long Id) {
		// TODO Auto-generated method stub
		return commonDAO.findOne(LogEntity.class, Id);
	}

	@Override
	public void deleteOne(LogEntity entity) {
		// TODO Auto-generated method stub
		commonDAO.delete(entity);
	}

	@Override
	public void Log(String user, String ip, String type, String title,
			String content) {
		// TODO Auto-generated method stub
		LogEntity log = new LogEntity();
		log.setUser(user);
		log.setTitle(title);
		log.setIp(ip);
		log.setType(type);
		log.setContent(content);
		log.setDate(DmDateUtil.Current());
		commonDAO.save(log);
	}

	@Override
	public void insert(LogEntity le) {
		// TODO Auto-generated method stub
		commonDAO.save(le);
	}

	@Override
	public PageInfo<Map> findLogs(Integer pageNum, Integer pageSize, Map argMap) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map> list = sqlSession
				.selectList("com.dm.platform.model.LogEntity.findLogs",argMap);
		PageInfo<Map> page = new PageInfo<Map>(list);
		return page;
	}

	@Override
	public void deleteAllLogs() {
		// TODO Auto-generated method stub
		sqlSession.delete("com.dm.platform.model.LogEntity.deleteAllLogs");
	}

}
