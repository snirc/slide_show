package snir.data.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao {

	private final JdbcTemplate jdbcTemplate;

	public ImageDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Loads an SQL file from the classpath and returns its content as a String.
	 *
	 * @param fileName the name of the SQL file (e.g., "create_schema.sql")
	 * @return the content of the SQL file as a String
	 * @throws IOException if an error occurs while reading the file
	 */
	public String getSqlFromFile(String fileName) throws IOException {
		// Load the file from the classpath
		Path filePath = new ClassPathResource("sql/" + fileName).getFile().toPath();
		String sql = Files.readString(filePath);
		return sql;
	}

	/**
	 * Creates the database schema if it does not exist by executing the SQL script.
	 *
	 * @param scriptFile the name of the SQL script file (e.g., "create_schema.sql")
	 */
	public void createSchemaIfNotExists(String scriptFile) {
		try {
			ClassPathResource resource = new ClassPathResource("sql/" + scriptFile);
			List<String> lines = Files.readAllLines(resource.getFile().toPath());
			StringBuilder sqlBuilder = new StringBuilder();
			for (String line : lines) {
				String trimmed = line.trim();
				if (trimmed.isEmpty() || trimmed.startsWith("--"))
					continue;
				sqlBuilder.append(line).append("\n");
				if (trimmed.endsWith(";")) {
					String sql = sqlBuilder.toString().trim();
					jdbcTemplate.execute(sql.substring(0, sql.length() - 1)); // remove trailing ;
					sqlBuilder.setLength(0); // reset buffer
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to run schema.sql", e);
		}
	}


	/**
	 * Saves an image record to the database.
	 *
	 * @param name     the name of the image
	 * @param url      the URL of the image
	 * @param duration the duration of the image in seconds
	 * @return the number of rows affected
	 */
	public int addImage(String name, String url, long duration) {
		String sql = "INSERT INTO slide.image (name, url, duration) VALUES (?, ?, ?)";
		return jdbcTemplate.update(sql, name, url, duration);
	}

	/**
	 * Saves a slide show record to the database.
	 *
	 * @param name  the name of the slide show
	 * @param email the email associated with the slide show
	 * @return the number of rows affected
	 */
	public int addSlideShow(String name) {
		String sql = "INSERT INTO slide.slide_show (name) VALUES (?)";
		return jdbcTemplate.update(sql, name);
	}


	/**
	 * Searches for images and their associated slide shows based on a keyword.
	 *
	 * @param keyword the keyword to search for in image names, URLs, or slide show
	 *                names
	 * @return a list of ImageSlideDTO objects containing image and slide show information
	 */
	public List<ImageSlideDTO> searchImages(String keyword) {
	    String sql = """
	        SELECT
	            i.id AS image_id,
	            i.name AS image_name,
	            i.url,
	            s.id AS slide_show_id,
	            s.name AS slide_show_name,
	            i.duration
	        FROM slide.image i
	        JOIN slide.slide_show_images si ON i.id = si.image_id
	        JOIN slide.slide_show s ON s.id = si.slide_show_id
	        WHERE i.name LIKE ? OR i.url LIKE ? OR s.name LIKE ?
	        ORDER BY i.id, s.id
	        """;

	    String searchTerm = "%" + keyword + "%";

	    return jdbcTemplate.query(
	        sql,
	        new Object[] { searchTerm, searchTerm, searchTerm },
	        (rs, rowNum) -> new ImageSlideDTO(
	            rs.getInt("image_id"),
	            rs.getString("image_name"),
	            rs.getString("url"),
	            rs.getInt("slide_show_id"),
	            rs.getString("slide_show_name"),
	            rs.getInt("duration") // Assuming duration is also selected
	        )
	    );
	}

	/**
	 * Adds images to a slide show by inserting records into the slide_show_images
	 * table.
	 *
	 * @param slideShowId the ID of the slide show
	 * @param imageIds    a list of image IDs to be added to the slide show
	 * @return an array of integers representing the number of rows affected for
	 *         each batch update
	 */
	public int[][] addImagesToSlideShow(int slideShowId, List<Integer> imageIds) {
		String sql = "INSERT INTO slide.slide_show_images (slide_show_id, image_id) VALUES (?, ?)";
		return jdbcTemplate.batchUpdate(sql, imageIds, imageIds.size(), (ps, imageId) -> {
			ps.setInt(1, slideShowId);
			ps.setInt(2, imageId);
		});
	}

	public List<ImageSlideDTO> getSlideImages(int slideShowId) {
		String sql = """
				SELECT
				    i.id AS image_id,
				    i.name AS image_name,
				    i.url,
				    s.id AS slide_show_id,
				    s.name AS slide_show_name,
				    i.duration
				FROM slide.image i
				JOIN slide.slide_show_images si ON i.id = si.image_id
				JOIN slide.slide_show s ON s.id = si.slide_show_id
				WHERE s.id = ?
				ORDER BY i.id
				""";

		return jdbcTemplate.query(sql, new Object[] { slideShowId },
				(rs, rowNum) -> new ImageSlideDTO(rs.getInt("image_id"), rs.getString("image_name"),
						rs.getString("url"), rs.getInt("slide_show_id"), rs.getString("slide_show_name"), rs.getInt("duration")));
	}
	
	/**
	 * Deletes an image by its ID from the database and removes it from all slide
	 * shows.
	 *
	 * @param imageId the ID of the image to be deleted
	 * @return the number of rows affected
	 */
	public int deleteImageById(int imageId) {
	    // Step 1: Remove image from all slideshows
	    String deleteFromSlides = "DELETE FROM slide.slide_show_images WHERE image_id = ?";
	    jdbcTemplate.update(deleteFromSlides, imageId);

	    // Step 2: Delete the image itself
	    String deleteImage = "DELETE FROM slide.image WHERE id = ?";
	    return jdbcTemplate.update(deleteImage, imageId);
	}
	
	/**
	 * Deletes a slide show by its ID from the database and removes all associated
	 * images.
	 *
	 * @param slideShowId the ID of the slide show to be deleted
	 * @return the number of rows affected
	 */
	public int deleteSlideShowById(int slideShowId) {
	    // Step 1: Remove all image associations from this slideshow
	    String deleteLinks = "DELETE FROM slide.slide_show_images WHERE slide_show_id = ?";
	    jdbcTemplate.update(deleteLinks, slideShowId);

	    // Step 2: Delete the slideshow itself
	    String deleteSlideShow = "DELETE FROM slide.slide_show WHERE id = ?";
	    return jdbcTemplate.update(deleteSlideShow, slideShowId);
	}

}






