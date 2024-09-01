package com.sakander;

import com.sakander.config.DatabaseConfig;
import com.sakander.model.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Sakander {
    public static void main(String[] args) {
        try {
            DatabaseConfig config = new DatabaseConfig();
            Database db = Database.connect(config);
            List<String[]> results = db.executeQueryWithRowsName("SELECT * FROM user");
            for (String[] row : results) {
                for (String column : row) {
                    System.out.print(column + "\t");
                }
                System.out.println();
            }
            db.close();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
