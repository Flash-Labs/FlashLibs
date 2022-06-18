package dev.flashlabs.flashlibs.database;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.SQLException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * Provides a interface for interacting with a database using a
 * {@link DataSource}, which can also be retrieved using a JDBC string. This
 * service provides methods for directly executing single SQL statements and
 * methods for obtaining connections for multiple SQL statements or managing
 * database transactions.
 */
public final class DatabaseService {

    private final DataSource source;

    private DatabaseService(DataSource source) {
        this.source = source;
    }

    /**
     * Creates a service backed by the given {@link DataSource}.
     */
    public static DatabaseService of(DataSource source) {
        return new DatabaseService(source);
    }

    /**
     * Creates a service from the given JDBC string using Sponge's provided
     * {@link SqlService}.
     *
     * @throws SQLException If database connection could not be established
     * @see SqlService#getDataSource(String)
     */
    public static DatabaseService of(String jdbc) throws SQLException {
        return new DatabaseService(Sponge.getServiceManager().provideUnchecked(SqlService.class).getDataSource(jdbc));
    }

    public DataSource getSource() {
        return source;
    }

    /**
     * Returns a reusable connection to the database.
     *
     * @throws SQLException If a database access error occurs
     */
    public Connection getConnection() throws SQLException {
        return new Connection(source.getConnection());
    }

    /**
     * Returns a reusable connection to the database that is designed for
     * managing database transactions.
     *
     * @throws SQLException If a database access error occurs
     */
    public Transaction getTransaction() throws SQLException {
        return new Transaction(source.getConnection());
    }

    /**
     * Opens a unique connection to execute an SQL statement with the given
     * arguments as a database modification, which does not return any results.
     *
     * @throws SQLException If a database error occurs
     */
    public void update(String sql, Object... args) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.update(sql, args);
        }
    }

    /**
     * Opens a unique connection to execute an SQL statement with the given
     * arguments as a database query and returns the retrieved results. The
     * results are cached in memory independent of a connection, since the
     * connection is discarded.
     *
     * @throws SQLException If a database error occurs
     * @see CachedRowSet
     * @see java.sql.ResultSet
     */
    public CachedRowSet query(String sql, Object... args) throws SQLException {
        try (Connection connection = getConnection()) {
            CachedRowSet cached = RowSetProvider.newFactory().createCachedRowSet();
            cached.populate(connection.query(sql, args));
            return cached;
        }
    }

}
