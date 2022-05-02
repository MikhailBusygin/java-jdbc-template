package com.epam.izh.rd.online.auction.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ComponentScan(basePackages = "com.epam.izh.rd.online.auction")
public class AppContextTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PublicAuction publicAuction;

    @Test
    @DisplayName("Бин DataSource успешно создан")
    public void dataSourceTest() {
        assertDoesNotThrow(() -> dataSource.getConnection().createStatement().execute("SELECT 1 FROM users"), "При доступе к БД произошла ошибка");
    }
}
