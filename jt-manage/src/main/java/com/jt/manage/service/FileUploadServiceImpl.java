package com.jt.manage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;

@Service
public class FileUploadServiceImpl implements FileUploadService {
	
	/**
	 * spring容器中如何动态的获取数据???
	 * @Value注解 实现数据的动态的获取
	 * 说明: @Value注解可以直接为String或基本类型赋值.
	 * 同时.可以利用Spring的机制可以为array list Map set properties赋值
	 * 有时间再说...
	 * 
	 * 注意事项:
	 * 	1.在SSM框架中,该注解需要依赖@Service注解(Spring容器内部才能使用)
	 *  2.如果使用SpringBoot则任何地方都能用
	 */
	@Value("${image.localDir}")
	private String localDir; //= "E:/jt-upload/";
	@Value("${image.urlPre}")
	private String urlPre;   // = "http://image.jt.com/";
	/**
	 *{"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
	 * error:0  是图片    error:1  表示不是图片
	 * url:   真实的物理路径/网站虚拟路径 image.jt.com
	 * width":图片的宽度,"height":图片的高度
	 * https://img20.360buyimg.com/vc/jfs/t19297/80/1703731107/320815/da350442/5ad44f3dNc1d94408.jpg"
	 * 
	 * 文件上传的思路
	 * 	1.判断文件的类型是否为图片
	 *  2.判断是否为恶意程序( 转化为字节码文件 ImageBuffer)
	 *  3.图片如何存储---分文件存储  作用:能够保证图片的量在一个可控范围内
	 *  4.应该将文件名称复杂化 UUID + 随机数999
	 *  5.实现文件上传
	 *  6.准备虚拟路径进行返回
	 */
	@Override
	public PicUploadResult uploadPic(MultipartFile uploadFile) {
		PicUploadResult result = new PicUploadResult();
		
		//1.判断是否为图片    abc.jpg
		String fileName = uploadFile.getOriginalFilename();
		if(!fileName.matches("^.*(jpg|png|gif)$")){
			
			result.setError(1);  //表示不是图片
			return result;
		}
		try {
			//2.判断是否为恶意程序
			BufferedImage bufferedImage = 
					ImageIO.read(uploadFile.getInputStream());
			//3.获取图片的宽度和高度
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			if(height == 0 || width == 0){
				result.setError(1);
				return result;
			}
			
			//4.准备文件存储的路径  通过动态注入方式获取
			
			
			//5.实现分文件存储 yyyy/MM/dd
			String dateDir = 
					new SimpleDateFormat("yyyy/MM/dd")
					.format(new Date());
			
			//6.形成保存图片的文件夹   E:/jt-upload/yyyy/MM/dd
			String picDir = localDir + dateDir;
			
			//7.生成文件夹
			File picFile = new File(picDir);
			if(!picFile.exists()){
				picFile.mkdirs();
			}
			
			//8.1为了让文件不重名 形成UUIDasdfasdfasdfasdfdfasdf
			String uuid = UUID.randomUUID()
					.toString().replace("-", "");
			
			//8.2添加随机数
			int randomNum = new Random().nextInt(999);
			
			//8.3获取文件后缀  abc.jpg
			String fileType = 
					fileName.substring(fileName.lastIndexOf("."));
			//8.4 形成最终文件名称   
			String realFileName = uuid + randomNum + fileType;
			
			//9.生成文件的本地磁盘路径全名  
			//E:/jt-upload/yyyy/MM/dd/werqwer100.jpg
			String localPath = picFile + "/" + realFileName;
			
			//10.实现文件上传
			uploadFile.transferTo(new File(localPath));
			
			//11.添加文件的宽度和高度
			result.setHeight(height+"");
			result.setWidth(width+"");
			
			//12.准备文件的虚拟路径   http://image.jt.com/2018/05/07/文件名称
			
			//通过动态取值的方式获取数据
			
			String urlPath = urlPre + dateDir + "/" + realFileName;
			result.setUrl(urlPath);
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(1);//表示为恶意程序
		}
		return result;
	}
}
