package snir.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "snir.data.dao")
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
    	String dockerEnv = System.getenv("DOCKER_ENV");

        String jdbcUrl;
        String username;
        String password;

        if (dockerEnv != null) {
            jdbcUrl = System.getenv("DB_URL");        // jdbc:mysql://mysql:3306
            username = System.getenv("DB_USERNAME");  // e.g. root
            password = System.getenv("DB_PASSWORD");  // e.g. shoko
        } else {
            jdbcUrl = "jdbc:mysql://localhost:3306";  // Default for local Eclipse run
            username = "root";
            password = "shoko";
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

