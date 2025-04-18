package snir.data.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
                if (trimmed.isEmpty() || trimmed.startsWith("--")) continue;
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
	 * @param name  the name of the image
	 * @param email the email associated with the image
	 * @return the number of rows affected
	 */
	public int addImage(String name, String email) {
		String sql = "INSERT INTO image (name, email) VALUES (?, ?)";
		return jdbcTemplate.update(sql, name, email);
	}
	
	/**
	 * Saves a slide show record to the database.
	 *
	 * @param name  the name of the slide show
	 * @param email the email associated with the slide show
	 * @return the number of rows affected
	 */
	public int addSlideShow(String name, String email) {
		String sql = "INSERT INTO slide.slide_show (name, email) VALUES (?, ?)";
		return jdbcTemplate.update(sql, name, email);
	}
	
	/**
	 * Saves a record in the slide_show_images table to associate an image with a slide show.
	 *
	 * @param imageId     the ID of the image
	 * @param slideShowId the ID of the slide show
	 * @return the number of rows affected
	 */
	public List<ImageSlideDTO> searchImages(String keyword) {
	    String sql = """
	        SELECT 
	            i.id AS image_id,
	            i.name AS image_name,
	            i.url,
	            s.id AS slide_show_id,
	            s.name AS slide_show_name
	        FROM image i
	        JOIN slide_show_images si ON i.id = si.image_id
	        JOIN slide_show s ON s.id = si.slide_show_id
	        WHERE i.name LIKE ? OR i.url LIKE ? OR s.name LIKE ?
	        ORDER BY i.id, s.id
	        """;

	    String searchTerm = "%" + keyword + "%";

	    return jdbcTemplate.query(sql, new Object[]{searchTerm, searchTerm, searchTerm}, (rs, rowNum) ->
	        new ImageSlideDTO(
	            rs.getInt("image_id"),
	            rs.getString("image_name"),
	            rs.getString("url"),
	            rs.getInt("slide_show_id"),
	            rs.getString("slide_show_name")
	        )
	    );
	}
}
