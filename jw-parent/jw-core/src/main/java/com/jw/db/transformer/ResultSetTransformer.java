package com.jw.db.transformer;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetTransformer {

    public <T> T execute(ResultSet rs) throws SQLException;
}
