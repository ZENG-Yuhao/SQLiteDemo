package com.example.enzo.sqlitedemo.DB;

import android.provider.BaseColumns;

/**
 * <p>
 * Created by ZENG Yuhao. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */

public final class DbContract {
    private DbContract() {
    }

    public static class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME   = "table_order";
        public static final String PRODUCT_NAME = "product_name";
        public static final String ORDER_PRICE  = "order_price";
        public static final String COUNTRY      = "country";
    }
}
