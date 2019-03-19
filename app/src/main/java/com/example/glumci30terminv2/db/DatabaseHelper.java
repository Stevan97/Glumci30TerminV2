package com.example.glumci30terminv2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.glumci30terminv2.db.model.Filmovi;
import com.example.glumci30terminv2.db.model.Glumac;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db.glumci.30.v2";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private Dao<Glumac, Integer> getmGlumacDao = null;
    private Dao<Filmovi, Integer> getmFilmoviDao = null;

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Glumac.class);
            TableUtils.createTable(connectionSource, Filmovi.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Filmovi.class, true);
            TableUtils.dropTable(connectionSource, Glumac.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Glumac, Integer> getGlumac() throws SQLException {
        if (getmGlumacDao == null) {
            getmGlumacDao = getDao(Glumac.class);
        }
        return getmGlumacDao;
    }

    public Dao<Filmovi, Integer> getFilmovi() throws SQLException {
        if (getmFilmoviDao == null) {
            getmFilmoviDao = getDao(Filmovi.class);
        }
        return getmFilmoviDao;
    }

    @Override
    public void close() {
        getmFilmoviDao = null;
        getmGlumacDao = null;
        super.close();
    }
}