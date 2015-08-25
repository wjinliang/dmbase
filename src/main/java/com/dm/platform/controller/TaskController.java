package com.dm.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/task")
public class TaskController extends DefaultController{
	
	@RequestMapping("/page")
	public ModelAndView taskCenter(ModelAndView model) {
		try {
			model.setViewName("/admin/task/page");
			return Model(model);
		} catch (Exception e) {
			e.printStackTrace();
			return Error(e);
		}
	}
}
