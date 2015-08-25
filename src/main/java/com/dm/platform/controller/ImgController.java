package com.dm.platform.controller;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dm.platform.util.RandomValidateCode;

@Controller
public class ImgController {

	@RequestMapping("/randomImage")
	public void randomImage(HttpServletRequest request,HttpServletResponse response) {
		try {
			BufferedImage is = RandomValidateCode.getInstance().getRandcode(request);
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "No-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("image/jpeg");
			ImageIO.write(is, "JPEG", response.getOutputStream()); // scaledImage1为BufferedImage，jpg为图像的类型
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}