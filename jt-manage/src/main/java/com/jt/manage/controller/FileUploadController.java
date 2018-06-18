package com.jt.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileUploadService;

@Controller
public class FileUploadController {
	
	//@Value(可能不好使)  //SpringMVC容器
	//private String url;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	/*实现文件上传
	 * 说明:MultipartFile 是SpringMVC中提供的文件上传的API
	 * 
	 * 1.判断文件的类型是否为图片
	 * 2.准备文件上传的文件路径
	 */
	@RequestMapping(value="/file",produces="text/html;charset=utf-8")
	@ResponseBody
	public String picUpload(MultipartFile pic) throws IllegalStateException, IOException {
		
		//1.文件上传的名称    abc.jpg
		String fileName = pic.getOriginalFilename();
		
		//2.判断是否问文件类型(jpg|png|gif)
		if(fileName.matches("^.*(jpg|png|gif)$")){
			
			//3.定义上传的路径
			String path = "E:/jt-upload";
			
			//4.判断是否有文件夹
			File file = new File(path);
			
			if(!file.exists()){
				//创建文件夹  创建多级目录
				file.mkdirs();
			}
			
			//E:/jt-upload/abc.jpg
			String fileNamePath = path + "/" + fileName;
			//5.实现文件上传
			pic.transferTo(new File(fileNamePath));
			
			System.out.println("好久不学了,忘的光光的!");
		}
		
		return "好久不学了,忘的光光的!";
	}
	
	
	
	//实现文件上传
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult uploadPic(MultipartFile uploadFile){
		
		return fileUploadService.uploadPic(uploadFile);
	}
}
