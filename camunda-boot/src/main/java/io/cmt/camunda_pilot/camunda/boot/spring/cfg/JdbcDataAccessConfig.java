package io.cmt.camunda_pilot.camunda.boot.spring.cfg;

import com.zaxxer.hikari.HikariDataSource;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ParametersAreNonnullByDefault
public class JdbcDataAccessConfig {

  public static final String PROC_ENG_DATASOURCE_NAME = "camundaBpmDataSource";

  /** Process engine data source configuration properties. */
  @Bean
  @ConfigurationProperties("jdbc.datasources.camunda-pilot-proc-eng")
  public DataSourceProperties procEngDataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Process engine data source. */
  @Bean(name = PROC_ENG_DATASOURCE_NAME)
  public DataSource procEngDataSource() {
    return procEngDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }
}
