package dev.flashlabs.flashlibs.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents a {@link PreparedStatement}, which contains a precompiled SQL
 * statement. Statements can be provided parameters and reused.
 */
public final class Statement {
    
    private final PreparedStatement statement;

    Statement(PreparedStatement statement) {
        this.statement = statement;
    }

    /**
     * Sets the parameters for this statement using the provided arguments.
     * Arguments are set in the same order as defined in the original statement.
     *
     * @throws SQLException If a database error occurs
     * @see PreparedStatement#setObject(int, Object) 
     */
    public Statement setParams(Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
        }
        return this;
    }

    /**
     * Executes this statement as a database modification, which does not return
     * any results.
     *
     * @throws SQLException If a database error occurs
     */
    public void update() throws SQLException {
        statement.executeUpdate();
    }

    /**
     * Executes this statement as a database query and returns the retrieved
     * results.
     *
     * @throws SQLException If a database error occurs
     */
    public ResultSet query() throws SQLException {
        return statement.executeQuery();
    }
    
}