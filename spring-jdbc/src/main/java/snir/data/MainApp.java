package snir.data;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import snir.data.dao.ImageDao;

public class MainApp {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

		
		  ImageDao imageDao = context.getBean(ImageDao.class);
		  try {
			imageDao.createSchemaIfNotExists("create_schema.sql");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 

		context.close();
	}
}
