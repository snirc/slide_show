package kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import snir.data.dao.ImageDao;

@Service
public class KafkaEventListener {

    private final ImageDao imageDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaEventListener(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @KafkaListener(topics = "proof-of-play-events", groupId = "slide-consumer-group")
    public void onMessage(String message) {
        try {
            JsonNode json = objectMapper.readTree(message);
            int slideShowId = json.get("slideShowId").asInt();
            int imageId = json.get("imageId").asInt();

            //imageDao.recordProofOfPlay(slideShowId, imageId);

			imageDao.addImage("kafka"+imageId, "http://kafka.jpg"+imageId, slideShowId);
			

			System.out.println("Handled Kafka message: " + message);

            
        } catch (Exception e) {
            System.err.println("Failed to handle Kafka message: " + message);
            e.printStackTrace();
        }
    }
}

