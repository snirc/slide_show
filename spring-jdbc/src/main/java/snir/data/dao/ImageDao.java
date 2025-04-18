package snir.data.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao {

	private final JdbcTemplate jdbcTemplate;

	public ImageDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void createSchemaIfNotExists(String schemaName) {
		String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
		jdbcTemplate.execute(sql);
	}

	int savImage(String name, String email) {
		String sql = "INSERT INTO image (name, email) VALUES (?, ?)";
		return jdbcTemplate.update(sql, name, email);
	}
}
