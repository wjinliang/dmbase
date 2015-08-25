package com.dm.platform.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;

/*******************************************************************************
 * 
 * @author wuzhenzhong
 * 
 * @since Feb 5, 2010
 * 
 ******************************************************************************/
public class FreeMarkertUtil {
	/**
	 * templatePath模板文件存放路径,templateName 模板文件名称,filename 生成的文件名称
	 * @param templatePath
	 * @param templateName
	 * @param fileName
	 * @param root
	 */
	public static void analysisTemplate(String templatePath,
			String templateName, String fileName, Map<?, ?> root,HttpServletRequest request) {
		try {
			//初使化FreeMarker配置
			Configuration config = new Configuration();
			// 设置要解析的模板所在的目录，并加载模板文件
			config.setDirectoryForTemplateLoading(new File(templatePath));
			// 设置包装器，并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());
			// 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致
			// 否则会出现乱码
			config=updateConfiguration(config,request);
			Template template = config.getTemplate(templateName, "UTF-8");
			
			// 合并数据模型与模板
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer out = new OutputStreamWriter(fos, "UTF-8");
			template.process(root, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}
	protected static Configuration updateConfiguration(Configuration configuration,HttpServletRequest request)
            throws TemplateException {
 
        // 设置标签类型([]、<>),[]这种标记解析要快些
        configuration.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
 
        // 设置允许属性为空
        configuration.setClassicCompatible(true);
        
        WebApplicationContext webApp=RequestContextUtils.getWebApplicationContext(request, request.getSession().getServletContext());
        // 获取实现TemplateDirectiveModel的bean
        Map<String, TemplateDirectiveModel> beans = webApp
                .getBeansOfType(TemplateDirectiveModel.class);
 
        for (String key : beans.keySet()) {
            Object obj = beans.get(key);
            if (obj != null && obj instanceof TemplateDirectiveModel) {
                configuration.setSharedVariable(key, obj);
            }
        }
 
        return configuration;
    }

}
