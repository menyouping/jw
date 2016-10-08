package com.jay.mvc.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jw.db.EntityUtils;
import com.jw.db.JwCallable;
import com.jw.db.JwTx;
import com.jw.db.SQLUtils;
import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Repository;

@Repository
public class WordRepository {

    public List<String> find(final String keyword) {
        ResultSet rs = JwTx.call(new JwCallable<ResultSet>() {

            @Override
            public ResultSet call(Connection connection) throws Exception {
                Map<String, Object> params = JwUtils.newHashMap();
                params.put("keyword", keyword + "%");

                String sql = "select k from jay_word where k like :keyword order by k asc limit 10";
                return SQLUtils.query(connection, sql, params);
            }
        });

        return EntityUtils.toStrings(rs);
    }
}
