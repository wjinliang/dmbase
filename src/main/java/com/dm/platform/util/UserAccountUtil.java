package com.dm.platform.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.dm.platform.dao.CommonDAO;
import com.dm.platform.model.Org;
import com.dm.platform.model.UserAccount;
import com.dm.platform.model.UserRole;

public class UserAccountUtil {
    private static UserAccountUtil instance = new UserAccountUtil();
    public WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
    public UserCache userCache = (UserCache) context.getBean("userCache");
    public CommonDAO commonDAO = (CommonDAO) context.getBean("commonDAOImpl");

    private UserAccountUtil() {

    }

    public static UserAccountUtil getInstance() {
        return instance;
    }

    public String getCurrentUser() {
        UserDetails userDetails =
            (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null)
            return userDetails.getUsername();
        return null;
    }


    public UserAccount getCurrentUserAccount() {
        UserAccount ua = (UserAccount) userCache.getUserFromCache(getCurrentUser());
        if (ua == null) {
            ua = (UserAccount) commonDAO
                .findByPropertyName(UserAccount.class, "loginname", getCurrentUser()).get(0);
            userCache.putUserInCache(ua);
        }
        return ua;
    }

    public String getCurrentUserId() {
        return getCurrentUserAccount().getCode();
    }

    public String getCurrentRoles() {
        String roleids = "";
        Set<UserRole> urset = getCurrentUserAccount().getRoles();
        for (UserRole userRole : urset) {
            roleids += userRole.getCode() + ",";
        }
        if (roleids.length() > 0) {
            roleids = roleids.substring(0, roleids.length() - 1);
        }
        return roleids;
    }

    public String getCurrentUserInboxName() {
        return (getCurrentUserAccount().getName() + "<" + getCurrentUserAccount().getEmail() + ">");
    }

    public String getUserNameById(String userId) {
        if (userId != null && !userId.equals("")) {
            UserAccount u = commonDAO.findOne(UserAccount.class, userId);
            if (u != null) {
                return u.getName();
            } else {
                return userId;
            }
        } else {
            return "-";
        }
    }

    public String getUserRemoteIpById(String userId) {
        if (userId != null && !userId.equals("")) {
            UserAccount u = commonDAO.findOne(UserAccount.class, userId);
            return u.getRemoteIpAddr();
        } else {
            return "-";
        }
    }

    public String getUserLastLoginTimeById(String userId) {
        if (userId != null && !userId.equals("")) {
            UserAccount u = commonDAO.findOne(UserAccount.class, userId);
            return u.getLastLoginTime();
        } else {
            return "-";
        }
    }


    public void kickUser(SessionRegistry sessionRegistry, String sessionId) {
        SessionInformation info = sessionRegistry.getSessionInformation(sessionId);
        if (info != null) {
            // 如果当前session失效了
            if (!info.isExpired()) {
                info.expireNow();
            }
        }
    }

    public List<Map<String, String>> listActiveUsers(SessionRegistry sessionRegistry) {
        Map<Object, Date> lastActivityDates = new HashMap<Object, Date>();
        List<Map<String, String>> mlist = new ArrayList<Map<String, String>>();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            UserAccount user = (UserAccount) principal;
            for (SessionInformation session : sessionRegistry.getAllSessions(principal, false)) {
                if (lastActivityDates.get(principal) == null) {
                    lastActivityDates.put(user.getCode(), session.getLastRequest());
                    Map map = new HashMap();
                    map.put("userCode", user.getCode());
                    map.put("userLoginname", user.getLoginname());
                    map.put("lastActivityDate",
                        DmDateUtil.DateToStr(session.getLastRequest(), "yyyy-MM-dd HH:mm:ss"));
                    map.put("sessionId", session.getSessionId());
                    mlist.add(map);
                } else {
                    Date prevLastRequest = lastActivityDates.get(user.getCode());
                    if (session.getLastRequest().after(prevLastRequest)) {
                        lastActivityDates.put(user.getCode(), session.getLastRequest());
                        Map map = new HashMap();
                        map.put("userCode", user.getCode());
                        map.put("userLoginname", user.getLoginname());
                        map.put("lastActivityDate",
                            DmDateUtil.DateToStr(session.getLastRequest(), "yyyy-MM-dd HH:mm:ss"));
                        map.put("sessionId", session.getSessionId());
                        mlist.add(map);
                    }
                }
            }
        }
        return mlist;
    }

    public boolean isOnline(SessionRegistry sessionRegistry, String userId) {
        boolean flag = false;
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            UserAccount user = (UserAccount) principal;
            if (userId.equals(user.getCode())) {
                flag = true;
            }
        }
        return flag;
    }

    public List getSessionIds(SessionRegistry sessionRegistry, String userId) {
        List sessionIds = new ArrayList();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            UserAccount user = (UserAccount) principal;
            if (user.getCode().equals(userId)) {
                for (SessionInformation session : sessionRegistry
                    .getAllSessions(principal, false)) {
                    sessionIds.add(session.getSessionId());
                }
            }
        }
        return sessionIds;
    }

    public Boolean isPermitOrg(String orgid) {
        List<String> ostring = new ArrayList<String>();
        UserAccount ua = getCurrentUserAccount();
        Org currentOrg = ua.getOrg();
        if (currentOrg != null) {
            ostring.add(String.valueOf(currentOrg.getId()));
            getDownOrgids(currentOrg, ostring);
        }
        if (ostring.contains(orgid)) {
            return true;
        } else {
            return false;
        }
    }

    public String getDownOrgidsStrs(String orgid) {
        List<String> ostring = new ArrayList<String>();
        Org currentOrg = commonDAO.findOne(Org.class, Long.valueOf(orgid));
        ostring.add(String.valueOf(currentOrg.getId()));
        getDownOrgids(currentOrg, ostring);
        String ids = "";
        for (int i = 0; i < ostring.size(); i++) {
            if (i == (ostring.size() - 1)) {
                ids += ostring.get(i);
            } else {
                ids += ostring.get(i) + ",";
            }
        }
        return ids;
    }

    private void getDownOrgids(Org org, List<String> ostr) {
        if (org.getChildren() != null && org.getChildren().size() > 0) {
            List<Org> olist = new ArrayList<Org>();
            olist = org.getChildren();
            for (Org org2 : olist) {
                ostr.add(String.valueOf(org2.getId()));
                getDownOrgids(org2, ostr);
            }
        }
    }

    public String getRequestIp(HttpServletRequest request) {
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }

        String[] arr = ipString.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str))
                ipString = str;
            break;
        }
        return ipString;
    }

    public String getCurrentIp() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebAuthenticationDetails wauth = (WebAuthenticationDetails) auth.getDetails();
        String ip = wauth.getRemoteAddress();
        return ip;
    }

    public Integer getOnlineCount(SessionRegistry sessionRegistry) {
        return sessionRegistry.getAllPrincipals().size();
    }
}
