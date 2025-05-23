package snir.data;

import java.util.Arrays;
import java.util.Date;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import snir.data.dao.ImageDao;

public class MainApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

		ImageDao imageDao = context.getBean(ImageDao.class);
		try {
			imageDao.createSchemaIfNotExists("create_schema.sql");
			runTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}

		context.close();
	}
	
	public static void runTest() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

		ImageDao imageDao = context.getBean(ImageDao.class);
		try {
			int randomNum = (int) (Math.random() * 100);
			imageDao.addImage("test1x"+randomNum, "http://test1x.jpg"+randomNum, randomNum);
			imageDao.addImage("test2x"+randomNum, "http://test2x.jpg"+randomNum, randomNum);
			imageDao.addImage("test3x"+randomNum, "http://test3x.jpg"+randomNum, randomNum);
			imageDao.addSlideShow("slide"+randomNum);
			
			/*
			 * Integer[] imageIds = { 4,5,6,7,8,9,10 }; imageDao.addImagesToSlideShow(4,
			 * Arrays.asList(imageIds)); System.out.println(imageDao.getSlideImages(3));
			 * System.out.println(imageDao.searchImages("test5"));
			 */
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		context.close();
	}
}
