package ru.opencu.springcourse.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Пример конфигурации для работы с несколькими БД.
 *
 *
 * Ключевые моменты:
 *   — Каждая DataSource имеет свой PlatformTransactionManager
 *   — @Transactional(transactionManager = "secondaryTxManager") указывает явно
 *   — @Primary определяет менеджер по умолчанию
 *   — Распределённые транзакции (XA/2PC) требуют JtaTransactionManager
 */
// @Configuration
public class MultipleDataSourceConfig {

    // ---- Primary DataSource (основная БД) -----------------------------------

    // @Bean @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    // @Bean @Primary
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    // @Bean @Primary
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("primaryDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }

    // ---- Secondary DataSource (вторая БД) -----------------------------------

    // @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    // @Bean("secondaryTxManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    // @Bean("secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("secondaryDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
