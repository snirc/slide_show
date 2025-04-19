package snir.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import snir.data.DataSourceConfig;

@SpringBootTest(classes = DataSourceConfig.class)
class ImageDaoTest {



	@Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ImageDao imageDao;

	@Test
	void testAddImage() {
		String name = "Test Image";
		String email = "test@example.com";
		when(jdbcTemplate.update(anyString(), eq(name), eq(email))).thenReturn(1);

		int rowsAffected = imageDao.addImage(name, email);

		assertEquals(1, rowsAffected);
		verify(jdbcTemplate, times(1)).update(anyString(), eq(name), eq(email));
	}

	@Test
	void testAddSlideShow() {
		String name = "Test Slide Show";
		String email = "test@example.com";
		when(jdbcTemplate.update(anyString(), eq(name), eq(email))).thenReturn(1);

		int rowsAffected = imageDao.addSlideShow(name, email);

		assertEquals(1, rowsAffected);
		verify(jdbcTemplate, times(1)).update(anyString(), eq(name), eq(email));
	}

	@Test
	void testSearchImages() {
		String keyword = "test";
		when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
	    .thenReturn(List.of(new ImageSlideDTO(1, "Image1", "url1", 1, "SlideShow1")));

		List<ImageSlideDTO> results = imageDao.searchImages(keyword);

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Image1", results.get(0).getImageName());
		assertEquals("url1", results.get(0).getImageUrl());
		assertEquals(1, results.get(0).getSlideShowId());
		assertEquals("SlideShow1", results.get(0).getSlideShowName());
		verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
	}

}
