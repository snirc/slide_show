package snir.data;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import snir.data.dao.ImageDao;

public class MainApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

		
		  ImageDao imageDao = context.getBean(ImageDao.class);
		  imageDao.createSchemaIfNotExists("slide112");
		 

		context.close();
	}
}
