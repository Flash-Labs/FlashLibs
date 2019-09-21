package dev.flashlabs.flashlibs.database;

import java.sql.SQLException;

/**
 * Represents a {@link Connection} for database transactions. This transaction
 * can be committed multiple times and will automatically rollback any
 * uncommitted statements when auto closed (such as on an exception).
 *
 * @see java.sql.Connection#setAutoCommit(boolean)
 */
public final class Transaction extends Connection {

    Transaction(java.sql.Connection connection) throws SQLException {
        super(connection);
        connection.setAutoCommit(false);
    }

    /**
     * Commits any statements executed since the previous commit.
     *
     * @throws SQLException If a database error occurs
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Closes this transaction and rolls back any uncommitted SQL statements.
     *
     * @throws SQLException If a database error occurs
     */
    @Override
    public void close() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
        connection.close();
    }

}