package app.Factory;

import java.sql.Connection;

public interface ConnectionManager {
    Connection createConnection();
    void closeConnection();
}
