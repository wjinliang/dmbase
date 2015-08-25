package com.dm.platform.util;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class JDBCUtil {
	private static ApplicationContext context = new ClassPathXmlApplicationContext(
			"config/spring/applicationContext.xml");
    public static JdbcTemplate getJdbcTemplate(){
    	JdbcTemplate jdbcTemplate =(JdbcTemplate)context.getBean("jdbcTemplate");
    	return jdbcTemplate;
    }
    
}
