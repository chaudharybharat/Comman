package com.aktivo.db;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by agc-linux on 13/9/17.
 */
@Database(name = Aktivodb.NAME, version = Aktivodb.VERSION)
public class Aktivodb {
    public static final String NAME = "aktivo";

    public static final int VERSION = 1;
}
