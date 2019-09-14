package dev.flashlabs.flashlibs.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a connection to the database. This class implements
 * {@link AutoCloseable}, which can be used with try-with-resources.
 */
public class Connection implements AutoCloseable {
    
    protected final java.sql.Connection connection;

    Connection(java.sql.Connection connection) {
        this.connection = connection;
    }

    /**
     * Creates a reusable statement prepared by the connection that allows for
     * parameters to be defined.
     *
     * @throws SQLException If a database error occurs
     * @see java.sql.Connection#prepareStatement(String)
     */
    public final Statement getStatement(String sql) throws SQLException {
        return new Statement(connection.prepareStatement(sql));
    }

    /**
     * Executes an SQL statement with the given arguments as a database
     * modification, which does not return any results.
     *
     * @throws SQLException If a database error occurs
     */
    public final void update(String sql, Object... args) throws SQLException {
        getStatement(sql).setParams(args).update();
    }

    /**
     * Executes an SQL statement with the given arguments as a database query
     * and returns the retrieved results.
     *
     * @throws SQLException If a database error occurs
     */
    public final ResultSet query(String sql, Object... args) throws SQLException {
        return getStatement(sql).setParams(args).query();
    }

    /**
     * Closes this connection.
     *
     * @throws SQLException If a database error occurs
     */
    @Override
    public void close() throws SQLException {
        connection.close();
    }
    
}