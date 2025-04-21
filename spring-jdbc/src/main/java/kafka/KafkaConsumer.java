package kafka;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import snir.data.DataSourceConfig;
import snir.data.dao.ImageDao;

public class KafkaConsumer {
	
	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

	
	@KafkaListener(topics = "proof-of-play-events")
	public void handleProofOfPlay(String messageJson){
		try {
			ImageDao imageDao = context.getBean(ImageDao.class);
			
			// Parse JSON (e.g., using Jackson)
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(messageJson);
			int slideShowId = node.get("slideShowId").asInt();
			int imageId = node.get("imageId").asInt();

			imageDao.deleteImageById(imageId);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
