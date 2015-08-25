package com.dm.platform.aop;

import java.lang.reflect.Method;
import java.util.Date;

import net.sf.ehcache.Cache;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.stereotype.Component;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.LogEntity;
import com.dm.platform.model.UserAccount;
import com.dm.platform.security.UserCacheUtil;
import com.dm.platform.util.DmDateUtil;
import com.dm.platform.util.EhCacheUtil;
import com.dm.platform.util.ResourceMapCache;
import com.dm.platform.util.UserAccountUtil;

@Component
@Aspect
public class LogAspect {
	private static Logger logger = Logger.getLogger(LogAspect.class);
	@Autowired
	private CommonDAO commonDAO;
	@Autowired
	private Cache myCache;
	@Autowired
	private UserCache userCache;

	@Pointcut("execution(* com.dm.platform.service.UserMenuService.refresh*(..))||"
			+ "execution(* com.dm.platform.service.UserRoleService.refresh*(..))||"
			+ "execution(* com.dm.platform.service.MenuGroupService.refresh*(..))||"
			+ "execution(* com.dm.platform.service.UserAccountService.refresh*(..))")
	public void refreshResourceMapCall() {
	}

	@Pointcut("execution(* com.dm.platform.service.UserAccountService.insert*(..))||execution(* com.dm.platform.service.UserAccountService.update*(..))||"
			+ "execution(* com.dm.platform.service.UserAccountService.delete*(..))")
	public void refreshUserCache() {
	}

	@Pointcut("execution(* com..controller.*.*(..))")
	public void allCall() {
	}

	@Pointcut("execution(* com.dm.platform.service.*.insert*(..))")
	public void insertServiceCall() {
	}

	@Pointcut("execution(* com.dm.platform.service.*.update*(..))")
	public void updateServiceCall() {
	}

	@Pointcut("execution(* com.dm.platform.service.*.delete*(..)) && !execution(* com.dm.platform.service.LogService.delete*(..))")
	public void deleteServiceCall() {
	}

	@AfterReturning(value = "refreshResourceMapCall()", argNames = "rtv", returning = "rtv")
	public void refreshResourceMapCall(JoinPoint joinPoint, Object rtv)
			throws Throwable {
		ResourceMapCache.getInstance().refreshResourceMap();
		EhCacheUtil.getInstance().refreshNavMenus(myCache);
	}

	@AfterReturning(value = "refreshUserCache()", argNames = "rtv", returning = "rtv")
	public void refreshUserCacheCall(JoinPoint joinPoint, Object rtv)
			throws Throwable {
		UserAccount targetUser = (UserAccount) joinPoint.getArgs()[0];
		UserCacheUtil.getInstance().refreshUserCache(userCache, targetUser);
	}

	@AfterReturning(value = "insertServiceCall()", argNames = "rtv", returning = "rtv")
	public void insertServiceCallCalls(JoinPoint joinPoint, Object rtv)
			throws Throwable {

		// 获取方法名
		String methodName = joinPoint.getSignature().getName();
		// 判断参数
		if (joinPoint.getArgs() != null) {
			joinPoint.getArgs()[0].getClass();
		}
		// 创建日志对象
		LogEntity log = new LogEntity();
		UserAccount user = UserAccountUtil.getInstance()
				.getCurrentUserAccount();
		log.setUser(user.getName() + "(" + user.getCode() + ")");
		log.setTitle(joinPoint.getTarget().getClass().getName() + "."
				+ methodName + "正常。");
		log.setContent(adminOptionContent(joinPoint.getArgs(), user.getName()
				+ ":新建"));
		log.setDate(DmDateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setType("1");
		log.setIp(UserAccountUtil.getInstance().getCurrentIp());
		commonDAO.save(log);
	}

	@AfterReturning(value = "updateServiceCall()", argNames = "rtv", returning = "rtv")
	public void updateServiceCallCalls(JoinPoint joinPoint, Object rtv)
			throws Throwable {

		// 获取方法名
		String methodName = joinPoint.getSignature().getName();
		// 判断参数
		if (joinPoint.getArgs() != null) {
			joinPoint.getArgs()[0].getClass();
		}
		// 创建日志对象
		LogEntity log = new LogEntity();
		UserAccount user = UserAccountUtil.getInstance()
				.getCurrentUserAccount();
		log.setUser(user.getName() + "(" + user.getCode() + ")");
		log.setTitle(joinPoint.getTarget().getClass().getName() + "."
				+ methodName + "正常。");
		log.setContent(adminOptionContent(joinPoint.getArgs(), user.getName()
				+ ":修改"));
		log.setDate(DmDateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setType("1");
		log.setIp(UserAccountUtil.getInstance().getCurrentIp());
		commonDAO.save(log);
	}

	@AfterReturning(value = "deleteServiceCall()", argNames = "rtv", returning = "rtv")
	public void deleteServiceCallCalls(JoinPoint joinPoint, Object rtv)
			throws Throwable {
		// 获取方法名
		String methodName = joinPoint.getSignature().getName();
		// 创建日志对象
		LogEntity log = new LogEntity();
		UserAccount user = UserAccountUtil.getInstance()
				.getCurrentUserAccount();
		log.setUser(user.getName() + "(" + user.getCode() + ")");
		log.setTitle(joinPoint.getTarget().getClass().getName() + "."
				+ methodName + "正常。");
		log.setContent(adminOptionContent(joinPoint.getArgs(), user.getName()
				+ ":删除"));
		log.setDate(DmDateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setType("1");
		log.setIp(UserAccountUtil.getInstance().getCurrentIp());
		commonDAO.save(log);
	}

	@AfterThrowing(value = "allCall()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, RuntimeException e) {
		LogEntity log = new LogEntity();
		UserAccount user = UserAccountUtil.getInstance()
				.getCurrentUserAccount();
		log.setUser(user.getName() + "(" + user.getCode() + ")");
		log.setTitle("产生异常的方法名称：  " + joinPoint.getSignature().getName());
		log.setContent("抛出的异常: " + e.getMessage().substring(0, 500));
		log.setDate(DmDateUtil.DateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		log.setType("1");
		log.setIp(UserAccountUtil.getInstance().getCurrentIp());
		commonDAO.save(log);
		logger.error(user.getName() + "(" + user.getCode() + ")"
				+ " 产生异常的方法名称：  " + joinPoint.getSignature().getName()
				+ " 抛出的异常: " + e.getMessage() + ">>>>>>>" + e.getCause());
	}

	@Around(value = "allCall()")
	public Object aroundCall(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		long procTime = System.currentTimeMillis();
		try {
			result = pjp.proceed();
			procTime = System.currentTimeMillis() - procTime;
			logger.info(pjp.getTarget().getClass().getName() + "."
					+ pjp.getSignature().getName() + "耗时：" + procTime + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String adminOptionContent(Object[] args, String mName)
			throws Exception {

		if (args == null) {
			return null;
		}

		StringBuffer rs = new StringBuffer();
		rs.append(mName);
		String className = null;
		// 遍历参数对象
		for (Object info : args) {
			// 获取对象类型
			className = info.getClass().getName();
			className = className.substring(className.lastIndexOf(".") + 1);
			rs.append("[类型：" + className + "，值：");
			// 获取对象的所有方法
			Method[] methods = info.getClass().getDeclaredMethods();
			// 遍历方法，判断get方法
			boolean flag = false;
			for (Method method : methods) {
				String methodName = method.getName();
				// 判断是不是get方法
				if ((methodName.indexOf("getId") == -1)
						&& (methodName.indexOf("getCode") == -1)) {// 不是get方法
					continue;// 不处理
				}

				Object rsValue = null;
				try {

					// 调用get方法，获取返回值
					rsValue = method.invoke(info);

					if (rsValue == null) {// 没有返回值
						continue;
					}

				} catch (Exception e) {
					continue;
				}

				// 将值加入内容中
				rs.append("(" + methodName.replace("get", "") + " : " + rsValue
						+ ")");
				flag = true;
			}
			if (!flag) {
				rs.append("(" + String.valueOf(info) + ")");
			}
			rs.append("]");
		}

		return rs.toString();
	}

}
