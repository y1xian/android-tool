package com.yyxnb.widget.db;

import com.yyxnb.widget.bean.TestData;

import java.util.List;

/**
 * 使用DBManager的好处是将数据库数据操作统一化，让业务类操作数据库只需要一个方法搞定，这样业务逻辑更清晰，同时数据库也更好维护
 */
public class DBManager {

    public static void insertTests(List<TestData> items) {
        AppDatabase.getInstance().testDao().insertItems(items);
    }

    public static void deleteTest(TestData test) {
        AppDatabase.getInstance().testDao().deleteItem(test);
    }

    public static void updateTest(TestData test) {
        AppDatabase.getInstance().testDao().updateItem(test);
    }

    public static List<TestData> getTests() {
        return AppDatabase.getInstance().testDao().getTests();
    }
}