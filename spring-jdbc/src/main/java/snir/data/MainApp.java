package snir.data;

import java.util.Arrays;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import snir.data.dao.ImageDao;

public class MainApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

		ImageDao imageDao = context.getBean(ImageDao.class);
		try {
			imageDao.createSchemaIfNotExists("create_schema.sql");
			
			/*
			 * imageDao.addImage("test1x", "http://test1x.jpg"); imageDao.addImage("test2x",
			 * "http://test2x.jpg"); imageDao.addImage("test3x", "http://test3x.jpg");
			 * imageDao.addSlideShow("slide2");
			 * 
			 * Integer[] imageIds = { 4, 5 }; imageDao.addImagesToSlideShow(2,
			 * Arrays.asList(imageIds)); System.out.println(imageDao.getSlideImages(2));
			 * System.out.println(imageDao.searchImages("test5"));
			 */
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.close();
	}
}
