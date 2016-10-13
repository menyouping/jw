package com.jw.db.transformer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ResultSetTransformerFactory {

    public static ResultSetTransformer getStringTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public String transform(ResultSet rs) throws SQLException {
                return rs.getString(1);
            }

        };
    }

    public static ResultSetTransformer getIntegerTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Integer transform(ResultSet rs) throws SQLException {
                return rs.getInt(1);
            }

        };
    }

    public static ResultSetTransformer getLongTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Long transform(ResultSet rs) throws SQLException {
                return rs.getLong(1);
            }

        };
    }

    public static ResultSetTransformer getDoubleTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Double transform(ResultSet rs) throws SQLException {
                return rs.getDouble(1);
            }

        };
    }

    public static ResultSetTransformer getFloatTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Float transform(ResultSet rs) throws SQLException {
                return rs.getFloat(1);
            }

        };
    }

    public static ResultSetTransformer getDateTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Date transform(ResultSet rs) throws SQLException {
                return rs.getDate(1);
            }

        };
    }

    public static ResultSetTransformer getBoolTransformer() {
        return new ResultSetTransformer() {

            @SuppressWarnings("unchecked")
            @Override
            public Boolean transform(ResultSet rs) throws SQLException {
                return rs.getBoolean(1);
            }

        };
    }
}
