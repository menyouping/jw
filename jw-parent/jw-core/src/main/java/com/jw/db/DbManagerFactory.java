package com.jw.db;

import com.jw.web.context.AppContext;

public class DbManagerFactory {

    public static DBManager getManager() {
        return AppContext.getBean("dbManager");
    }

}
