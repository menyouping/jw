package com.jw.db.transformer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ResultSetTransformerFactory {

    public static ResultSetTransformer getStringTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public String execute(ResultSet rs) throws SQLException {
                return rs.getString(1);
            }

        };
    }

    public static ResultSetTransformer getIntegerTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Integer execute(ResultSet rs) throws SQLException {
                return rs.getInt(1);
            }

        };
    }

    public static ResultSetTransformer getLongTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Long execute(ResultSet rs) throws SQLException {
                return rs.getLong(1);
            }

        };
    }

    public static ResultSetTransformer getDoubleTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Double execute(ResultSet rs) throws SQLException {
                return rs.getDouble(1);
            }

        };
    }

    public static ResultSetTransformer getFloatTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Float execute(ResultSet rs) throws SQLException {
                return rs.getFloat(1);
            }

        };
    }

    public static ResultSetTransformer getDateTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Date execute(ResultSet rs) throws SQLException {
                return rs.getDate(1);
            }

        };
    }
}
