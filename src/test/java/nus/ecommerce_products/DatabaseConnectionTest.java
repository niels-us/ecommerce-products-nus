package nus.ecommerce_products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@SpringBootTest
public class DatabaseConnectionTest {
    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("✅ Connessione al database riuscita!");
            System.out.println("Database: " + connection.getCatalog());
            System.out.println("URL: " + connection.getMetaData().getURL());
            System.out.println("Username: " + connection.getMetaData().getUserName());

        }

    }

}
