package com.dm.platform.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dm.cms.model.ChannelEntity;
import com.dm.platform.model.UserMenu;

public class PageUtil {
	private static PageUtil instance = new PageUtil();
	private PageUtil(){
	}
	public static PageUtil getInstance(){
		return instance;
	}
	public Comparator<UserMenu> c = new Comparator<UserMenu>() {
		 public int compare(UserMenu o1, UserMenu o2) {
	           return (int) (o1.getSeq()-o2.getSeq());
	    }
	};
	
	public static String pagestr(int thispage, int totalpage ,long totalcount,int pagesize) {
		StringBuffer stb = new StringBuffer();
		stb.append("<span class='ui-item-link'>");
		stb.append("<div>");
		stb.append("<a href='javascript:gopage(0);' class='ui-page-item ui-page-item-first'>首页</a>");
		if(thispage==0){
			stb.append("<a href='javascript:prepage();' class='ui-page-item ui-page-item-prev ui-page-item-disable'>上一页</a>");
		}else{
			stb.append("<a href='javascript:prepage();' class='ui-page-item ui-page-item-prev'>上一页</a>");
		}
		
		if((thispage==totalpage-1)||(totalpage==0)){
			stb.append("<a href='javascript:nextpage();' class='ui-page-item ui-page-next ui-page-item-disable'>下一页</a>");
		}else{
			stb.append("<a href='javascript:nextpage();' class='ui-page-item ui-page-next'>下一页</a>");
		}
		stb.append("<a href='javascript:gopage("+(totalpage-1)+");' class='ui-page-item ui-page-last'>尾页</a>"); 
		stb.append("</div>");
		stb.append("<div class='ui-page-go'>");
		stb.append("<h4>转到</h4><input id='jumpp' type='text' class='input-page' /><a href='javascript:jumppage();'  class='page-go'>Go</a>");
		stb.append("</div>");
		stb.append("</span>");
		stb.append("<span class='ui-page-item ui-page-item-set'><h4>每页</h4>");
		stb.append("<select name='pagesize' id='pagesize' class='select-page' onchange='javascript:search()'>");
		if(pagesize==10){
			stb.append("<option selected='selected' value='10'>10</option>");
		}else{
			stb.append("<option value='10'>10</option>");
		}
		if(pagesize==20){
			stb.append("<option selected='selected' value='20'>20</option>");
		}else{
			stb.append("<option value='20'>20</option>");
		}
		if(pagesize==30){
			stb.append("<option selected='selected' value='30'>30</option>");
		}else{
			stb.append("<option value='30'>30</option>");
		}
		if(pagesize==40){
			stb.append("<option selected='selected' value='40'>40</option>");
		}else{
			stb.append("<option value='40'>40</option>");
		}
		if(pagesize==50){
			stb.append("<option selected='selected' value='50'>50</option>");
		}else{
			stb.append("<option value='50'>50</option>");
		}
		stb.append("</select>");
		stb.append("</span>");
		if(totalpage>0){
			stb.append("<span class='ui-page-item ui-page-item-info'>共"+totalcount+"条记录，共"+(thispage+1)+"/"+totalpage+"页，第"+(thispage+1)+"页</span>");
		}else{
			stb.append("<span class='ui-page-item ui-page-item-info'>共"+totalcount+"条记录，共"+thispage+"/"+totalpage+"页，第"+thispage+"页</span>");
		}
		return stb.toString();
	}
	
	public String pagination(int thispage, int totalpage ,long totalcount,int pagesize) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='row-fluid'>");
		sb.append("<div class='span6'>");
		sb.append("<div class='dataTables_info'>");
		if(totalpage==0){
			sb.append("第0/0页,"+"共0条记录。");
		}else{
			sb.append("第"+(thispage+1)+"/"+totalpage+"页,"+"共"+totalcount+"条记录。");
		}
		sb.append("</div>");
		sb.append("</div>");
		sb.append("<div class='span6'>");
		sb.append("<div class='dataTables_paginate paging_bootstrap pagination'>");
		sb.append("<ul>");
		sb.append("<li class='prev'>");
		sb.append("<a href='javascript:gopage(0);'><span class='hidden-480'>首页</span></a>");
		sb.append("</li>");
		if(thispage==0){
			sb.append("<li class='prev disabled'>");
		}else{
			sb.append("<li class='prev'>");
		}
		sb.append("<a href='javascript:prepage();'><span class='hidden-480'>上一页</span></a>");
		sb.append("</li>");
		if((thispage==totalpage-1)||(totalpage==0)){
			sb.append("<li class='next disabled'>");
		}else{
			sb.append("<li class='next'>");
		}
		sb.append("<a href='javascript:nextpage();'><span class='hidden-480'>下一页</span></a>");
		sb.append("</li>");
		sb.append("<li class='next'>");
		sb.append("<a href='javascript:gopage("+(totalpage-1)+");'><span class='hidden-480'>尾页</span></a>");
		sb.append("</li>");
		sb.append("<li><input type='text' data-title='page' style='width:20px;height:20px;float:left'/><a data-title='go' href='javascript:;'><span class='hidden-480'>跳转</span></a></li>");
		sb.append("</ul>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	
	public String channelPagination(ChannelEntity channel,int thispage,long totalcount,int pagesize) {
		String cId = channel.getChannelId().toString();
		String isHtml = channel.getIsStaticPage()==null?"0":channel.getIsStaticPage();
		String htmlDir = channel.getStaticPageDir();
		Integer totalpage = (int) (totalcount / pagesize);
		if (totalcount % pagesize != 0) {
			totalpage++;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"pagination pagination-right\">");
		sb.append("<ul>");
		if(thispage==1){
			sb.append("<li class='disabled'><a href='avascript:;'>首页</a></li>");
			sb.append("<li class='prev disabled'><a href='javascript:;'>上一页</a></li>");
		}else{
			int newPage = thispage-1;
			String href;
			if(newPage==1){
				href = isHtml.equals("1")?(htmlDir+"/"+cId+".html"):(cId+".htm");
			}else{
				href = isHtml.equals("1")?(htmlDir+"/"+cId+"_"+newPage+".html"):(cId+"_"+newPage+".htm");
			}
			sb.append("<li><a href='"+(isHtml.equals("1")?(htmlDir+"/"+cId+".html"):(cId+".htm"))+"'>首页</a></li>");
			sb.append("<li class='prev'><a href="+href+">上一页</a></li>");
		}
		sb = getChannelPaginationLis(cId,isHtml,htmlDir,sb,thispage,totalpage);
		if((thispage==totalpage)||(totalpage==0)){
			sb.append("<li class='next disabled'><a href='javascript:;'>下一页</a></li>");
			sb.append("<li class='disabled'><a href='javascript:;'>尾页</a></li>");
		}else{
			int newPage = thispage+1;
			String href;
			if(newPage==1){
				href = isHtml.equals("1")?(htmlDir+"/"+cId+".html"):(cId+".htm");
			}else{
				href = isHtml.equals("1")?(htmlDir+"/"+cId+"_"+newPage+".html"):(cId+"_"+newPage+".htm");
			}
			sb.append("<li class='prev'><a href="+href+">下一页</a></li>");
			sb.append("<li><a href='"+(isHtml.equals("1")?(htmlDir+"/"+cId+"_"+totalpage+".html"):(cId+"_"+totalpage+".htm"))+"'>尾页</a></li>");
		}
		sb.append("</ul>");
		sb.append("</div>");
		return sb.toString();
	}
	
	private StringBuffer getChannelPaginationLis(String cId,String isHtml,String htmlDir,StringBuffer sb,int thispage, int totalpage){
		int start;
		int end;
		if(totalpage<5){
			start = 1;
			end = totalpage;
		}else{
			if(thispage<3){
				start = 1;
				end = 5;
			}else{
				if(thispage<totalpage-2){
					start = thispage-2;
					end = thispage+2;
				}else{
					start = totalpage-4;
					end = totalpage;
				}
			}
		}
		for(int i=start;i<=end;i++){
			String href = "";
			if(i==1){
				href = isHtml.equals("1")?(htmlDir+"/"+cId+".html"):(cId+".htm");;
			}else{
				href = isHtml.equals("1")?(htmlDir+"/"+cId+"_"+i+".html"):(cId+"_"+i+".htm");;
			}
			if(i==thispage){
				sb.append("<li class='active'><a href='javascript:;'>"+i+"</a><li>");
			}else{
				sb.append("<li><a href='"+href+"'>"+i+"</a><li>");
			}
		}
		return sb;
	} 
	
	public String formatMenus(Set<UserMenu> menus) {
		StringBuffer str = new StringBuffer();
		List<UserMenu> topMenus = new ArrayList<UserMenu>();
		for (UserMenu userMenu : menus) {
			if (!menus.contains(userMenu.getPuserMenu())) {
				topMenus.add(userMenu);
			}
		}
		Collections.sort(topMenus,c);
		for (UserMenu userMenu : topMenus) {
			str.append("<li id='li_"+userMenu.getId()+"' title='"+userMenu.getName()+"'");
			if (hasChild(userMenu)) {
				str.append(">");
				str.append("<a href='javascript:void(0);'>");
			}else{
				str.append(" name='leaf_li' >");
				str.append("<a href=\"javascript:go('"+userMenu.getUrl()+"')\">");
			}
			str.append("<i class='" + userMenu.getIcon() + "'></i> ");
			str.append("<span class='title'>" + userMenu.getName() + "</span>");

			if (hasChild(userMenu)) {
				str.append("<span class='arrow'></span>");
				str.append("</a>");
				//递归遍历
				str=appendSubMenus(userMenu,str);
			}else{
				str.append("<span id='span_"+userMenu.getId()+"'></span>");
				str.append("</a>");
			}
			str.append("</li>");

		}
		return str.toString();
	}
	
	private boolean hasChild(UserMenu m) {
		Set<UserMenu> mset = EhCacheUtil.getInstance().getNavMenus(UserAccountUtil.getInstance().getCurrentRoles());
		if (m.getChildren().size() > 0) {
			for (UserMenu userMenu : mset) {
				if (m.getChildren().contains(userMenu)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}

	}
	
	private Set<UserMenu> getChildren(UserMenu m){
		Set<UserMenu> childrenSet = new HashSet<UserMenu>();
		Set<UserMenu> mset =EhCacheUtil.getInstance().getNavMenus(UserAccountUtil.getInstance().getCurrentRoles());
		if (m.getChildren().size() > 0) {
			for (UserMenu userMenu : mset) {
				if (m.getChildren().contains(userMenu)) {
					childrenSet.add(userMenu);
				}
			}
		}
		return childrenSet;
	}
	
	private StringBuffer appendSubMenus(UserMenu m,StringBuffer str) {
		if (hasChild(m)) {
			str.append("<ul class='sub-menu'>");
			Set<UserMenu> childrenSet = getChildren(m);
			//使用arraylist排序
			List<UserMenu> childrenList = new ArrayList<UserMenu>();
			for (UserMenu userMenu : childrenSet) {
				childrenList.add(userMenu);
			}
			Collections.sort(childrenList,c);
			for (UserMenu userMenu : childrenList) {
				str.append("<li id='li_"+userMenu.getId()+"' title='"+userMenu.getName()+"' ");
				if (hasChild(userMenu)){
					str.append(">");
					str.append("<a href='javascript:void(0);'>");
					str.append("<i class='" + userMenu.getIcon() + "'></i> ");
					str.append("<span class='arrow'></span>");
					str.append(userMenu.getName());
					str.append("</a>");
					str=appendSubMenus(userMenu,str);
				}else{
					str.append(" name='leaf_li'>");
					str.append("<a href=\"javascript:go('"+userMenu.getUrl()+"')\">");
					str.append("<i class='" + userMenu.getIcon() + "'></i> ");
					str.append(userMenu.getName());
					str.append("</a>");
				}
				str.append("</li>");
			}
			str.append("</ul>");
		}
		return str;
	}
	
	public String formatHorizontalMenus(Set<UserMenu> menus) {
		StringBuffer str = new StringBuffer();
		
		str.append("<ul class='nav'>");
		List<UserMenu> topMenus = new ArrayList<UserMenu>();
		for (UserMenu userMenu : menus) {
			if (!menus.contains(userMenu.getPuserMenu())) {
				topMenus.add(userMenu);
			}
		}
		Collections.sort(topMenus,c);
		for (UserMenu userMenu : topMenus) {
			str.append("<li id='h_li_"+userMenu.getId()+"' title='"+userMenu.getName()+"'");
			if (hasChild(userMenu)) {
				str.append(">");
				str.append("<a data='h_top_a' data-toggle='dropdown' class='dropdown-toggle' href='javascript:;'>");
			}else{
				str.append(" name='leaf_li' >");
				str.append("<a href=\"javascript:go('"+userMenu.getUrl()+"')\">");
			}
			str.append(userMenu.getName());

			if (hasChild(userMenu)) {
				str.append("<span class='arrow'></span>");
				str.append("</a>");
				//递归遍历
				str=appendHorizontalSubMenus(userMenu,str);
			}else{
				str.append("</a>");
			}
			str.append("</li>");

		}
		str.append("</ul>");
		
		return str.toString();
	}
	
	private StringBuffer appendHorizontalSubMenus(UserMenu m,StringBuffer str) {
		if (hasChild(m)) {
			str.append("<ul class='dropdown-menu'>");
			Set<UserMenu> childrenSet = getChildren(m);
			//使用arraylist排序
			List<UserMenu> childrenList = new ArrayList<UserMenu>();
			for (UserMenu userMenu : childrenSet) {
				childrenList.add(userMenu);
			}
			Collections.sort(childrenList,c);
			for (UserMenu userMenu : childrenList) {
				str.append("<li id='h_li_"+userMenu.getId()+"' title='"+userMenu.getName()+"' ");
				if (hasChild(userMenu)){
					str.append(" class='dropdown-submenu'> ");
					str.append("<a href='javascript:;'>");
					str.append(userMenu.getName());
					str.append("<span class='arrow'></span>");
					str.append("</a>");
					str=appendHorizontalSubMenus(userMenu,str);
				}else{
					str.append(" name='leaf_li'>");
					str.append("<a href=\"javascript:go('"+userMenu.getUrl()+"')\">");
					str.append(userMenu.getName());
					str.append("</a>");
				}
				str.append("</li>");
			}
			str.append("</ul>");
		}
		return str;
	}
}
