package com.antarmarg;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionTest {

    @Test
    public void testPostgresConnection() {
        String[] hosts = {
            "dpg-db4em6vlk1mc73fps0ug-a.singapore-postgres.render.com",
            "dpg-db4em6vlk1mc73fps0ug-a.oregon-postgres.render.com",
            "dpg-db4em6vlk1mc73fps0ug-a.frankfurt-postgres.render.com"
        };

        for (String host : hosts) {
            String url = "jdbc:postgresql://" + host + ":5432/tt_wbyu?sslmode=require";
            try {
                System.out.println("Attempting connection to " + host + "...");
                Connection conn = DriverManager.getConnection(url, "tt_wbyu_user", "82mYVNCwvXBDTYrsTVk505bGgqaNSPi4");
                System.out.println("SUCCESSFULLY CONNECTED TO: " + host);
                conn.close();
                return;
            } catch (Exception e) {
                System.out.println("Failed on " + host + ": " + e.getMessage());
            }
        }
    }
}
