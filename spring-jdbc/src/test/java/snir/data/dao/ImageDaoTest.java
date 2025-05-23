package snir.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
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
        String url = "test@example.com";
        long duration = 10;

        when(jdbcTemplate.update(anyString(), eq(name), eq(url), eq(duration))).thenReturn(1);

        int rowsAffected = imageDao.addImage(name, url, duration);

        assertEquals(1, rowsAffected);
        verify(jdbcTemplate, times(1)).update(anyString(), eq(name), eq(url), eq(duration));
    }


	@Test
	void testAddSlideShow() {
		String name = "Test Slide Show";
		
		when(jdbcTemplate.update(anyString(), eq(name))).thenReturn(1);

		int rowsAffected = imageDao.addSlideShow(name);

		assertEquals(1, rowsAffected);
		verify(jdbcTemplate, times(1)).update(anyString(), eq(name));
	}

	public List<ImageSlideDTO> getSlideImages(int slideShowId) {
    String sql = """
            SELECT
                i.id AS image_id,
                i.name AS image_name,
                i.url,
                s.id AS slide_show_id,
                s.name AS slide_show_name
            FROM slide.image i
            JOIN slide.slide_show_images si ON i.id = si.image_id
            JOIN slide.slide_show s ON s.id = si.slide_show_id
            WHERE s.id = ?
            ORDER BY i.id
            """;

    return jdbcTemplate.query(sql, new Object[] { slideShowId },
            (rs, rowNum) -> new ImageSlideDTO(
                    rs.getInt("image_id"),
                    rs.getString("image_name"),
                    rs.getString("url"),
                    rs.getInt("slide_show_id"),
                    rs.getString("slide_show_name"),
                    rs.getInt("duration")
            ));
}

	
	@Test
	void testSearchImages() {
		String keyword = "test";
		when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
	    .thenReturn(List.of(new ImageSlideDTO(1, "Image1", "url1", 1, "SlideShow1", 10)));

		List<ImageSlideDTO> results = imageDao.searchImages(keyword);

		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals("Image1", results.get(0).getImageName());
		assertEquals("url1", results.get(0).getImageUrl());
		assertEquals(1, results.get(0).getSlideShowId());
		assertEquals("SlideShow1", results.get(0).getSlideShowName());
		verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
	}
	
	@Test
	void testDeleteImageById() {
	    int imageId = 1;

	    when(jdbcTemplate.update(contains("slide_show_images"), eq(imageId))).thenReturn(2); // 2 links removed
	    when(jdbcTemplate.update(contains("image"), eq(imageId))).thenReturn(1); // 1 image deleted

	    int result = imageDao.deleteImageById(imageId);

	    assertEquals(1, result); // We expect 1 image row to be deleted
	    verify(jdbcTemplate, times(1)).update(contains("slide_show_images"), eq(imageId));
	    verify(jdbcTemplate, times(1)).update(contains("slide.image"), eq(imageId));

	}

	@Test
	void testDeleteSlideShowById() {
	    int slideShowId = 10;

	    when(jdbcTemplate.update(contains("slide_show_images"), eq(slideShowId))).thenReturn(3); // 3 links removed
	    when(jdbcTemplate.update(contains("slide_show"), eq(slideShowId))).thenReturn(1); // slideshow deleted

	    int result = imageDao.deleteSlideShowById(slideShowId);

	    assertEquals(1, result); // Expect 1 slideshow deleted
	    verify(jdbcTemplate, times(1)).update(contains("slide_show_images"), eq(slideShowId));
	    verify(jdbcTemplate, times(1)).update(eq("DELETE FROM slide.slide_show WHERE id = ?"), eq(slideShowId));

	}

}
