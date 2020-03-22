package com.jay.mvc.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jw.aop.annotation.Query;
import com.jw.db.EntityUtils;
import com.jw.db.JwCallable;
import com.jw.db.JwRunnable;
import com.jw.db.JwTx;
import com.jw.db.SQLUtils;
import com.jw.util.CollectionUtils;
import com.jw.web.bind.annotation.Repository;

@Repository
public class WordRepository {

    @Query("select * from dict_word where k=? limit 1")
    public Word find(String keyword) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Query("select k,v from dict_word where type = ? and k like ? order by k asc limit 10")
    public List<Map> find(int type, String keyword) {
        return null;
    }

    public Map<String, Object> findKV(final Integer id) {
        return JwTx.call(new JwCallable<Map<String, Object>>() {

            @Override
            public Map<String, Object> call(Connection connection) throws Exception {
                ResultSet rs = SQLUtils.query("select k,v, version from dict_word where id = ?", new Object[] { id });
                return CollectionUtils.first(EntityUtils.toMaps(rs));
            }
        });
    }

    public String find(final Integer id) {
        return JwTx.call(new JwCallable<String>() {

            @Override
            public String call(Connection connection) throws Exception {
                ResultSet rs = SQLUtils.query("select k from dict_word where id = ?", new Object[] { id });
                return CollectionUtils.first(EntityUtils.toStrings(rs));
            }
        });
    }

    public void update(final Word word) {
        JwTx.run(new JwRunnable() {

            @Override
            public void run(Connection connection) throws Exception {
                SQLUtils.update("update dict_word set v = ?, version = ? where id = ?",
                        new Object[] { word.getV(), word.getVersion(), word.getId() });
            }
        });
    }
}
