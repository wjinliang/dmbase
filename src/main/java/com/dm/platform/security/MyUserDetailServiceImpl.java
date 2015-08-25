package com.dm.platform.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.UserAccount;

public class MyUserDetailServiceImpl implements UserDetailsService{

	@Resource
	CommonDAO commonDAO;
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub
        UserAccount user = null;
        if(commonDAO.findByPropertyName(UserAccount.class, "loginname", username).size()>0){
        	user=commonDAO.findByPropertyName(UserAccount.class, "loginname", username).get(0);
        }else{
        	throw new UsernameNotFoundException("用户名不存在！");
        }  
        return user;
	}

}
